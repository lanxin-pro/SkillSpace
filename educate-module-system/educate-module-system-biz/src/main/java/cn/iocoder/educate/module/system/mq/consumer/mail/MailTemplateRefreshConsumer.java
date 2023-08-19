package cn.iocoder.educate.module.system.mq.consumer.mail;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.mail.MailTemplateRefreshMessage;
import cn.iocoder.educate.module.system.service.mail.MailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link MailTemplateRefreshMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 13:34
 */
@Component
@Slf4j
public class MailTemplateRefreshConsumer extends AbstractChannelMessageListener<MailTemplateRefreshMessage> {

    @Resource
    private MailTemplateService mailTemplateService;

    @Override
    public void onMessage(MailTemplateRefreshMessage message) {
        log.info("[onMessage][收到 Mail Template 刷新信息]");
        mailTemplateService.initLocalCache();
    }

}
