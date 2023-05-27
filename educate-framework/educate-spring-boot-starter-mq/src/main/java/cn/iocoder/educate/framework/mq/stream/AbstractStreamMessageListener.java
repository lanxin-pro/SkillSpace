package cn.iocoder.educate.framework.mq.stream;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/26 13:25
 */
public abstract class AbstractStreamMessageListener<T extends AbstractStreamMessage>
        implements StreamListener<String, ObjectRecord<String,String>> {

    /**
     * 处理消息
     *
     * @param message 消息
     */
    public abstract void onMessage(T message);

    @Override
    public void onMessage(ObjectRecord<String, String> message) {

    }

}
