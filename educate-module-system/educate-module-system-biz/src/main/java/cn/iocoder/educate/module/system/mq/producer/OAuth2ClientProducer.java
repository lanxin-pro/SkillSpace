package cn.iocoder.educate.module.system.mq.producer;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.module.system.mq.message.auth.OAuth2ClientRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OAuth 2.0 客户端相关消息的 Producer
 *
 * @Author: j-sentinel
 * @Date: 2023/8/7 11:23
 */
@Component
public class OAuth2ClientProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link OAuth2ClientRefreshMessage} 消息
     */
    public void sendOAuth2ClientRefreshMessage() {
        OAuth2ClientRefreshMessage message = new OAuth2ClientRefreshMessage();
        redisMQTemplate.send(message);
    }

}
