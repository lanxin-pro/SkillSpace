package cn.iocoder.educate.module.system.service.sms;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.sms.core.client.SmsClient;
import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.template.SmsTemplateCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.template.SmsTemplatePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.template.SmsTemplateUpdateReqVO;
import cn.iocoder.educate.module.system.convert.sms.SmsTemplateConvert;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsTemplateMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.producer.sms.SmsProducer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 短信模板 Service 接口
 *
 * @Author: 董伟豪
 * @Date: 2023/5/27 20:10
 */
@Slf4j
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService{

    /**
     * 正则表达式，匹配 {} 中的变量
     * 通过正则表达式提取出花括号中的内容
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    /**
     * 短信模板缓存
     * key：短信模板编码 {@link SmsTemplateDO#getCode()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     *
     */
    @Getter // 为了方便测试，这里提供 getter 方法
    private volatile Map<String,SmsTemplateDO> smsTemplateCache;

    @Resource
    private SmsChannelService smsChannelService;

    @Resource
    private SmsProducer smsProducer;

    @Resource
    private SmsClientFactory smsClientFactory;

    @Override
    @PostConstruct
    public void initLocalCache() {
        List<SmsTemplateDO> smsTemplateList = smsTemplateMapper.selectList(new QueryWrapper<>());
        log.info("[initLocalCache][缓存短信模版，数量为:{}]", smsTemplateList.size());
        smsTemplateCache = smsTemplateList.stream().collect(Collectors.toMap(SmsTemplateDO::getCode,
                Function.identity(),(oldValue, newValue) -> newValue));
    }

    @Override
    public SmsTemplateDO getSmsTemplateByCodeFromCache(String code) {
        return smsTemplateCache.get(code);
    }

    @Override
    public String formatSmsTemplateContent(String content, Map<String, Object> templateParams) {
        return StrUtil.format(content, templateParams);
    }

    @Override
    public Long countByChannelId(Long channelId) {
        return smsTemplateMapper.selectCountByChannelId(channelId);
    }

    @Override
    public Long createSmsTemplate(SmsTemplateCreateReqVO smsTemplateCreateReqVO) {
        // 校验短信渠道
        SmsChannelDO channelDO = validateSmsChannel(smsTemplateCreateReqVO.getChannelId());
        // 校验短信编码是否重复
        validateSmsTemplateCodeDuplicate(null, smsTemplateCreateReqVO.getCode());
        // 校验短信模板
        validateApiTemplate(smsTemplateCreateReqVO.getChannelId(), smsTemplateCreateReqVO.getApiTemplateId());

        // 插入
        SmsTemplateDO template = SmsTemplateConvert.INSTANCE.convert(smsTemplateCreateReqVO);
        // 参数数组
        template.setParams(parseTemplateContentParams(template.getContent()));
        template.setChannelCode(channelDO.getCode());
        smsTemplateMapper.insert(template);
        // 发送刷新消息
        smsProducer.sendSmsTemplateRefreshMessage();
        // 返回
        return template.getId();
    }

    @Override
    public void updateSmsTemplate(SmsTemplateUpdateReqVO smsTemplateUpdateReqVO) {
        // 校验存在
        validateSmsTemplateExists(smsTemplateUpdateReqVO.getId());
        // 校验短信渠道
        SmsChannelDO channelDO = validateSmsChannel(smsTemplateUpdateReqVO.getChannelId());
        // 校验短信编码是否重复
        validateSmsTemplateCodeDuplicate(smsTemplateUpdateReqVO.getId(), smsTemplateUpdateReqVO.getCode());
        // 校验短信模板
        validateApiTemplate(smsTemplateUpdateReqVO.getChannelId(), smsTemplateUpdateReqVO.getApiTemplateId());

        // 更新
        SmsTemplateDO smsTemplateDO = SmsTemplateConvert.INSTANCE.convert(smsTemplateUpdateReqVO);
        smsTemplateDO.setParams(parseTemplateContentParams(smsTemplateDO.getContent()));
        smsTemplateDO.setChannelCode(channelDO.getCode());
        smsTemplateMapper.updateById(smsTemplateDO);
        // 发送刷新消息
        smsProducer.sendSmsTemplateRefreshMessage();
    }

    @Override
    public void deleteSmsTemplate(Long id) {
        // 校验存在
        validateSmsTemplateExists(id);
        // 更新
        smsTemplateMapper.deleteById(id);
        // 发送刷新消息
        smsProducer.sendSmsTemplateRefreshMessage();
    }

    @Override
    public SmsTemplateDO getSmsTemplate(Long id) {
        return smsTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<SmsTemplateDO> getSmsTemplatePage(SmsTemplatePageReqVO smsTemplatePageReqVO) {
        return smsTemplateMapper.selectPage(smsTemplatePageReqVO);
    }

    private void validateSmsTemplateExists(Long id) {
        if (smsTemplateMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_TEMPLATE_NOT_EXISTS);
        }
    }

    public SmsChannelDO validateSmsChannel(Long channelId) {
        // 查询短信渠道是否存在
        SmsChannelDO channelDO = smsChannelService.getSmsChannel(channelId);
        if (channelDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CHANNEL_NOT_EXISTS);
        }
        // 开启状态的判断
        if (!Objects.equals(channelDO.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CHANNEL_DISABLE);
        }
        return channelDO;
    }

    /**
     * @param id 在添加操作的时候 id 是空
     * @param code
     */
    public void validateSmsTemplateCodeDuplicate(Long id, String code) {
        SmsTemplateDO template = smsTemplateMapper.selectByCode(code);
        // 如果为空直接返回
        if (template == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
        // 存在相同的模板
        if (!template.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
    }

    /**
     * @param channelId 渠道id
     * @param apiTemplateId api远程的短信模板
     */
    public void validateApiTemplate(Long channelId, String apiTemplateId) {
        // 获得短信模板客户端
        SmsClient smsClient = smsClientFactory.getSmsClient(channelId);
        Assert.notNull(smsClient, String.format("短信客户端(%d) 不存在", channelId));
        SmsCommonResult<SmsTemplateRespDTO> templateResult = smsClient.getSmsTemplate(apiTemplateId);
        // 校验短信模板是否正确
        templateResult.checkError();
    }

    public List<String> parseTemplateContentParams(String content) {
        // 正则相关工具类  取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

}
