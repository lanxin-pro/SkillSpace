package cn.iocoder.educate.framework.mq.core;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.mq.stream.AbstractStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
     * 发送 Redis 消息，基于 Redis Stream 实现
     * @param message 消息
     * @param <T> Redis Stream Message 抽象类
     * @return 消息记录的编号对象
     */
    public <T extends AbstractStreamMessage> RecordId send(T message){
        try{
            sendMessageBefore(message);
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

    private <T extends AbstractStreamMessage> void sendMessageAfter(T message) {
    }

    private <T extends AbstractStreamMessage> void sendMessageBefore(T message) {
    }


}
