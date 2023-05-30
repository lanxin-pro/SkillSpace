package cn.iocoder.educate.module.system.mq.consumer.sms;

import cn.iocoder.educate.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.educate.module.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.educate.module.system.service.sms.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * 针对 {@link SmsSendMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/5/26 13:23
 */
@Slf4j
@Component
public class SmsSendConsumer extends AbstractStreamMessageListener<SmsSendMessage> {

    @Resource
    private SmsSendService smsSendService;

    protected SmsSendConsumer() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    }

    @Override
    public void onMessage(SmsSendMessage message) {
        log.info("[onMessage][消息内容({})]",message);
        smsSendService.doSendSms(message);
    }
}
