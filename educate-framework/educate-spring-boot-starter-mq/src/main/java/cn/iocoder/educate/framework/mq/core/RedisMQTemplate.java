package cn.iocoder.educate.framework.mq.core;

import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.mq.core.message.AbstractRedisMessage;
import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import cn.iocoder.educate.framework.mq.core.stream.AbstractStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis MQ 操作模板类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/26 10:47
 */
@AllArgsConstructor
public class RedisMQTemplate {

    @Getter
    private final RedisTemplate<String, ?> redisTemplate;

    /**
     * 发送 Redis 消息，基于 Redis pub/sub 实现
     *
     * @param message
     * @param <T>
     */
    public <T extends AbstractChannelMessage> void send(T message) {
        try {
            sendMessageBefore(message);
            // 发送消息
            redisTemplate.convertAndSend(message.getChannel(), JsonUtils.toJsonString(message));
        } finally {
            sendMessageAfter(message);
        }
    }

    /**
     * 发送 Redis 消息，基于 Redis Stream 实现
     * @param message 消息
     * @param <T> Redis Stream Message 抽象类
     * @return 消息记录的编号对象
     */
    public <T extends AbstractStreamMessage> RecordId send(T message){
        try{
            sendMessageBefore(message);
            // TODO j-sentinel 这个代码有可能无法发送消息
            // 发送消息
            return redisTemplate.opsForStream() // 获取到 StreamOperations 对象
                    .add(
                    StreamRecords.newRecord() // 创建了一条新的消息记录
                    .ofObject(JSONUtil.toJsonStr(message)) // 设置内容
                    .withStreamKey(message.getStreamKey())); // 设置 Redis Stream key
        }finally {
            sendMessageAfter(message);
        }

    }

    private <T extends AbstractStreamMessage> void sendMessageAfter(AbstractRedisMessage message) {
    }

    private <T extends AbstractStreamMessage> void sendMessageBefore(AbstractRedisMessage message) {
    }


}
