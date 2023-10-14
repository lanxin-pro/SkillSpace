package cn.iocoder.educate.module.system.mq.consumer.notify;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.notify.NotifyTemplateRefreshMessage;
import cn.iocoder.educate.module.system.service.notify.NotifyTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link NotifyTemplateRefreshMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/10/11 20:50
 */
@Component
@Slf4j
public class NotifyTemplateRefreshConsumer extends AbstractChannelMessageListener<NotifyTemplateRefreshMessage> {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Override
    public void onMessage(NotifyTemplateRefreshMessage message) {
        log.info("[onMessage][收到 NotifyTemplate 刷新消息]");
        notifyTemplateService.initLocalCache();
    }

}
