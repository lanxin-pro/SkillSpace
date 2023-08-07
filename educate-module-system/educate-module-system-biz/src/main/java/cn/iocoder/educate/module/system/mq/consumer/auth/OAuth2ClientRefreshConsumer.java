package cn.iocoder.educate.module.system.mq.consumer.auth;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.auth.OAuth2ClientRefreshMessage;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link OAuth2ClientRefreshMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/8/7 11:24
 */
@Component
@Slf4j
public class OAuth2ClientRefreshConsumer extends AbstractChannelMessageListener<OAuth2ClientRefreshMessage> {

    @Resource
    private OAuth2ClientService oauth2ClientService;

    @Override
    public void onMessage(OAuth2ClientRefreshMessage message) {
        log.info("[onMessage][收到 OAuth2Client 刷新消息]");
        oauth2ClientService.initLocalCache();
    }

}
