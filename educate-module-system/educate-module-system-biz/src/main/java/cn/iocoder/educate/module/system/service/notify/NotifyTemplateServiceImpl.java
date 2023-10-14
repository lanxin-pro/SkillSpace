package cn.iocoder.educate.module.system.service.notify;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.template.NotifyTemplateCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.template.NotifyTemplatePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.template.NotifyTemplateUpdateReqVO;
import cn.iocoder.educate.module.system.convert.notify.NotifyTemplateConvert;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.notify.NotifyTemplateMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.producer.notify.NotifyProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 站内信模版 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/11 20:33
 */
@Service
@Validated
@Slf4j
public class NotifyTemplateServiceImpl implements NotifyTemplateService {

    /**
     * 正则表达式，匹配 {} 中的变量
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Resource
    private NotifyTemplateMapper notifyTemplateMapper;

    /**
     * 站内信模板缓存
     * key：站内信模板编码 {@link NotifyTemplateDO#getCode()}
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Map<String, NotifyTemplateDO> notifyTemplateCache;

    @Resource
    private NotifyProducer notifyProducer;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<NotifyTemplateDO> templates = notifyTemplateMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存站内信模版，数量为:{}]", templates.size());

        // 第二步：构建缓存
        notifyTemplateCache = templates.stream().collect(Collectors.toMap(NotifyTemplateDO::getCode,
                Function.identity(),(v1,v2) -> v1));
    }

    @Override
    public NotifyTemplateDO getNotifyTemplateByCodeFromCache(String code) {
        return notifyTemplateCache.get(code);
    }

    @Override
    public Long createNotifyTemplate(NotifyTemplateCreateReqVO createReqVO) {
        // 校验站内信编码是否重复
        validateNotifyTemplateCodeDuplicate(null, createReqVO.getCode());

        // 插入
        NotifyTemplateDO notifyTemplate = NotifyTemplateConvert.INSTANCE.convert(createReqVO);
        // 根据规则生成一个List<String>数组
        notifyTemplate.setParams(parseTemplateContentParams(notifyTemplate.getContent()));
        notifyTemplateMapper.insert(notifyTemplate);

        // 发送刷新消息
        notifyProducer.sendNotifyTemplateRefreshMessage();
        return notifyTemplate.getId();
    }

    @Override
    public void updateNotifyTemplate(NotifyTemplateUpdateReqVO updateReqVO) {
        // 校验存在
        validateNotifyTemplateExists(updateReqVO.getId());
        // 校验站内信编码是否重复
        validateNotifyTemplateCodeDuplicate(updateReqVO.getId(), updateReqVO.getCode());

        // 更新
        NotifyTemplateDO updateObj = NotifyTemplateConvert.INSTANCE.convert(updateReqVO);
        updateObj.setParams(parseTemplateContentParams(updateObj.getContent()));
        notifyTemplateMapper.updateById(updateObj);

        // 发送刷新消息
        notifyProducer.sendNotifyTemplateRefreshMessage();
    }

    @Override
    public void deleteNotifyTemplate(Long id) {
        // 校验存在
        validateNotifyTemplateExists(id);
        // 删除
        notifyTemplateMapper.deleteById(id);
        // 发送刷新消息
        notifyProducer.sendNotifyTemplateRefreshMessage();
    }

    @Override
    public NotifyTemplateDO getNotifyTemplate(Long id) {
        return notifyTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<NotifyTemplateDO> getNotifyTemplatePage(NotifyTemplatePageReqVO pageReqVO) {
        return notifyTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public String formatNotifyTemplateContent(String content, Map<String, Object> params) {
        return StrUtil.format(content, params);
    }

    @VisibleForTesting
    public void validateNotifyTemplateCodeDuplicate(Long id, String code) {
        NotifyTemplateDO template = notifyTemplateMapper.selectByCode(code);
        if (template == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTIFY_TEMPLATE_CODE_DUPLICATE, code);
        }
        if (!template.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTIFY_TEMPLATE_CODE_DUPLICATE, code);
        }
    }

    private void validateNotifyTemplateExists(Long id) {
        if (notifyTemplateMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTIFY_TEMPLATE_NOT_EXISTS);
        }
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
     *
     * @param content
     * @return
     */
    @VisibleForTesting
    public List<String> parseTemplateContentParams(String content) {
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

}
