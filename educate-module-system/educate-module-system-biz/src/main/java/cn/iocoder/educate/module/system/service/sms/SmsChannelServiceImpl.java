package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import cn.iocoder.educate.module.system.convert.sms.SmsChannelConvert;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsChannelMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.producer.sms.SmsProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/28 19:12
 */
@Slf4j
@Service
public class SmsChannelServiceImpl implements SmsChannelService{

    @Resource
    private SmsChannelMapper smsChannelMapper;

    @Resource
    private SmsClientFactory smsClientFactory;

    @Getter // 为了方便测试，这里提供 getter 方法
    private volatile Map<String, SmsChannelDO> smsChannelCache;

    @Resource
    private SmsProducer smsProducer;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Override
    @PostConstruct
    public void initLocalCache() {
        List<SmsChannelDO> channelDOS = smsChannelMapper.selectList(new QueryWrapper<>());
        log.info("[initLocalCache][缓存短信渠道，数量为:{}]", channelDOS.size());
        // TODO j-sentinel 短信模板这里需要代码优化
        List<SmsChannelProperties> smsChannelProperties = SmsChannelConvert.INSTANCE.convertListToProperties(channelDOS);
        smsChannelProperties.forEach(smsChannelProperties1 -> {
            smsClientFactory.createOrUpdateSmsClient(smsChannelProperties1);
        });
    }

    @Override
    public SmsChannelDO getSmsChannel(Long channelId) {
        return smsChannelMapper.selectById(channelId);
    }

    @Override
    public Long createSmsChannel(SmsChannelCreateReqVO createReqVO) {
        // 插入
        SmsChannelDO smsChannel = SmsChannelConvert.INSTANCE.convert(createReqVO);
        smsChannelMapper.insert(smsChannel);
        // 发送刷新消息
        smsProducer.sendSmsChannelRefreshMessage();
        // 返回
        return smsChannel.getId();
    }

    @Override
    public void updateSmsChannel(SmsChannelUpdateReqVO updateReqVO) {
        // 校验存在
        validateSmsChannelExists(updateReqVO.getId());
        // 更新
        SmsChannelDO updateObj = SmsChannelConvert.INSTANCE.convert(updateReqVO);
        smsChannelMapper.updateById(updateObj);
        // 发送刷新消息
        smsProducer.sendSmsChannelRefreshMessage();
    }

    @Override
    public void deleteSmsChannel(Long id) {
        // 校验存在
        validateSmsChannelExists(id);
        // 校验是否有在使用该账号的模版
        if (smsTemplateService.countByChannelId(id) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CHANNEL_HAS_CHILDREN);
        }
        // 删除
        smsChannelMapper.deleteById(id);
        // 发送刷新消息
        smsProducer.sendSmsChannelRefreshMessage();
    }

    @Override
    public List<SmsChannelDO> getSmsChannelList() {
        return smsChannelMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO smsChannelPageReqVO) {
        return smsChannelMapper.selectPage(smsChannelPageReqVO);
    }

    private void validateSmsChannelExists(Long id) {
        if (smsChannelMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CHANNEL_NOT_EXISTS);
        }
    }

}
