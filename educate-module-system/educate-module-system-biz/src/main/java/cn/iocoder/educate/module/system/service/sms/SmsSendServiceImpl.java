package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.educate.module.system.mq.producer.sms.SmsProducer;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;

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

    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // 校验短信模板是否合法
        SmsTemplateDO template = validateSmsTemplate(templateCode);
        // 校验短信渠道是否合法
        validateSmsChannel(template.getChannelId());
        //smsProducer.sendSmsMessage();
        return null;
    }



    /**
     * 注：此方法只给consumers调用
     * @param message
     */
    @Override
    public void doSendSms(SmsSendMessage message) {

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

    private SmsChannelDO validateSmsChannel(Long channelId) {
        // 获得短信模板。考虑到效率，从缓存中获取
        smsChannelService.getSmsChannel(channelId);
        return null;
    }
}
