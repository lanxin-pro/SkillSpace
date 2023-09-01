package cn.iocoder.educate.module.mp.mq.producer;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.module.mp.mq.message.MpAccountRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 公众号账号 Producer
 *
 * @Author: j-sentinel
 * @Date: 2023/9/1 11:23
 */
@Component
public class MpAccountProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link MpAccountRefreshMessage} 消息
     */
    public void sendAccountRefreshMessage() {
        MpAccountRefreshMessage message = new MpAccountRefreshMessage();
        redisMQTemplate.send(message);
    }

}
