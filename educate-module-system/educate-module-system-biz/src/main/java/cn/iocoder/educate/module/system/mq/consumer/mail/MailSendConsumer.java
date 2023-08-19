package cn.iocoder.educate.module.system.mq.consumer.mail;

import cn.iocoder.educate.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.educate.module.system.mq.message.mail.MailSendMessage;
import cn.iocoder.educate.module.system.service.mail.MailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * 针对 {@link MailSendMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 15:05
 */
@Component
@Slf4j
public class MailSendConsumer extends AbstractStreamMessageListener<MailSendMessage> {

    @Resource
    private MailSendService mailSendService;

    protected MailSendConsumer() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    }

    @Override
    public void onMessage(MailSendMessage message) {
        log.info("[onMessage][消息内容({})]", message);
        mailSendService.doSendMail(message);
    }

}
