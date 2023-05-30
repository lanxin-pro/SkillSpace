package cn.iocoder.educate.framework.mq.core.stream;

import cn.hutool.core.util.TypeUtil;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * Redis Stream 监听器抽象类，用于实现集群消费
 *
 * @param <T> 消息类型。一定要填写噢，不然会报错
 *
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

    /**
     * 消息类型
     */
    private final Class<T> messageType;

    /**
     * Redis Channel
     */
    @Getter
    private final String streamKey;

    /**
     * Redis 消费者分组，默认使用 spring.application.name 名字
     */
    @Value("${spring.application.name}")
    @Getter
    private String group;

    /**
     * RedisMQTemplate
     */
    @Setter
    private RedisMQTemplate redisMQTemplate;


    protected AbstractStreamMessageListener() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.messageType = getMessageClass();
        this.streamKey = messageType.getDeclaredConstructor().newInstance().getStreamKey();
    }

    private Class<T> getMessageClass() {
        Type type = TypeUtil.getTypeArgument(getClass(), 0);
        if (type == null) {
            throw new IllegalStateException(String.format("类型(%s) 需要设置消息类型", getClass().getName()));
        }
        return (Class<T>) type;
    }

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        // 消费消息
        T messageObj = JsonUtils.parseObject(message.getValue(), messageType);
    }

}
