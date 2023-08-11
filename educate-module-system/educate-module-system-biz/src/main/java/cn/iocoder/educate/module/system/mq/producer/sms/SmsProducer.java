package cn.iocoder.educate.module.system.mq.producer.sms;

import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.module.system.mq.message.sms.SmsChannelRefreshMessage;
import cn.iocoder.educate.module.system.mq.message.sms.SmsSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/26 13:03
 */
@Slf4j
@Component
public class SmsProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link SmsSendMessage} 消息
     * @param logId 短信日志编号
     * @param mobile 手机号
     * @param channelId 渠道编号
     * @param apiTemplateId 短信模板编号
     * @param templateParams 短信模板参数
     */
    public void sendSmsMessage(Long logId, String mobile,
                               Long channelId, String apiTemplateId, List<KeyValue<String, Object>> templateParams){
        SmsSendMessage message = new SmsSendMessage().setLogId(logId).setMobile(mobile)
                .setChannelId(channelId).setApiTemplateId(apiTemplateId).setTemplateParams(templateParams);
        redisMQTemplate.send(message);
    }

    /**
     * 发送 {@link SmsChannelRefreshMessage} 消息
     */
    public void sendSmsChannelRefreshMessage() {
        SmsChannelRefreshMessage message = new SmsChannelRefreshMessage();
        redisMQTemplate.send(message);
    }

}
