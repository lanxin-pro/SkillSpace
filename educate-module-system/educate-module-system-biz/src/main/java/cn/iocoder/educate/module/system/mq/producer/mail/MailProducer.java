package cn.iocoder.educate.module.system.mq.producer.mail;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.module.system.mq.message.mail.MailAccountRefreshMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Mail 邮件相关消息的 Producer
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 9:52
 */
@Slf4j
@Component
public class MailProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link MailAccountRefreshMessage} 消息
     */
    public void sendMailAccountRefreshMessage() {
        MailAccountRefreshMessage message = new MailAccountRefreshMessage();
        redisMQTemplate.send(message);
    }

}
