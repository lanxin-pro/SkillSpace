package cn.iocoder.educate.module.system.service.sms;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.sms.core.client.SmsClient;
import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.educate.module.system.mq.producer.sms.SmsProducer;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短信发送 Service 发送的实现
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:50
 */
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Resource
    private SmsProducer smsProducer;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private SmsChannelService smsChannelService;

    @Resource
    private SmsLogService smsLogService;

    @Resource
    private SmsClientFactory smsClientFactory;

    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // 校验短信模板是否合法
        SmsTemplateDO template = validateSmsTemplate(templateCode);
        // 校验短信渠道是否合法
        SmsChannelDO smsChannelDO = validateSmsChannel(template.getChannelId());
        // 校验手机号参数是否存在
        validateMobile(mobile);
        // 构建有序的模板参数。为什么放在这个位置，是提前保证模板参数的正确性，而不是到了插入发送日志
        List<KeyValue<String, Object>> newTemplateParams = buildTemplateParams(template, templateParams);
        // 创建发送日志。如果模板被禁用，则不发送短信，只记录日志
        Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus())
                && CommonStatusEnum.ENABLE.getStatus().equals(smsChannelDO.getStatus());

        String content = smsTemplateService.formatSmsTemplateContent(template.getContent(), templateParams);
        Long sendLogId = smsLogService.createSmsLog(mobile, userId, userType, isSend, template, content, templateParams);
        // 发送 MQ 消息，异步执行发送短信
        if(isSend){
            smsProducer.sendSmsMessage(sendLogId, mobile, template.getChannelId(),
                    template.getApiTemplateId(), newTemplateParams);
        }
        return sendLogId;
    }



    /**
     * 注：此方法只给consumers调用
     * @param message
     */
    @Override
    public void doSendSms(SmsSendMessage message) {
        SmsClient smsClient = smsClientFactory.getSmsClient(message.getChannelId());
        // 获得渠道对应的 SmsClient 客户端
        Assert.notNull(smsClient, "短信客户端({}) 不存在", message.getChannelId());
        // 发送短信
        SmsCommonResult<SmsSendRespDTO> smsSendRespDTOSmsCommonResult = smsClient.sendSms(message.getLogId(),
                message.getMobile(), message.getApiTemplateId(), message.getTemplateParams());
    }

    @Override
    public Long sendSingleSmsToAdmin(String mobile, Long userId, String templateCode, Map<String, Object> templateParams) {
        return null;
    }

    /**
     * 校验短信模板是否合法
     * @param templateCode
     * @return
     */
    private SmsTemplateDO validateSmsTemplate(String templateCode) {
        // 获得短信模板。考虑到效率，从缓存中获取，防止缓存穿透
        SmsTemplateDO templateDO = smsTemplateService.getSmsTemplateByCodeFromCache(templateCode);
        if(templateDO == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_SEND_TEMPLATE_NOT_EXISTS);
        }
        return templateDO;
    }

    /**
     * 校验短信渠道
     * @param channelId
     * @return
     */
    private SmsChannelDO validateSmsChannel(Long channelId) {
        // 获得短信模板。考虑到效率，从缓存中获取
        SmsChannelDO smsChannelDO = smsChannelService.getSmsChannel(channelId);
        // 短信模板不存在
        if(smsChannelDO == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CHANNEL_NOT_EXISTS);
        }
        return smsChannelDO;
    }

    /**
     * 校验手机号
     * @param mobile
     * @return
     */
    public String validateMobile(String mobile) {
        if(StrUtil.isEmpty(mobile)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_SEND_MOBILE_NOT_EXISTS);
        }
        return mobile;
    }

    /**
     * 将参数模板，处理成有序的 KeyValue 数组
     *
     * 原因是，部分短信平台并不是使用 key 作为参数，而是数组下标，
     * 例如说腾讯云 https://cloud.tencent.com/document/product/382/39023
     *
     * @param template 短信模板
     * @param templateParams 原始参数
     * @return 处理后的参数
     */
    List<KeyValue<String, Object>> buildTemplateParams(SmsTemplateDO template, Map<String, Object> templateParams) {
        return template.getParams().stream().map(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_SEND_MOBILE_TEMPLATE_PARAM_MISS, key);
            }
            return new KeyValue<>(key, value);
        }).collect(Collectors.toList());
    }

}
