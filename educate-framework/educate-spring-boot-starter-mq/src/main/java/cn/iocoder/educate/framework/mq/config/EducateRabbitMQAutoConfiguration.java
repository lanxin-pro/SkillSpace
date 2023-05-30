package cn.iocoder.educate.framework.mq.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import cn.iocoder.educate.framework.common.enums.DocumentEnum;
import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.framework.mq.core.stream.AbstractStreamMessage;
import cn.iocoder.educate.framework.mq.core.stream.AbstractStreamMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.DefaultStreamMessageListenerContainerX;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.List;
import java.util.Properties;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/25 14:14
 */
@Slf4j
@Configuration
@EnableScheduling // 启用定时任务，用于 RedisPendingMessageResendJob 重发消息
public class EducateRabbitMQAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate stringRedisTemplate) {
        return new RedisMQTemplate(stringRedisTemplate);
    }

    /**
     * Redis Pub/Sub 广播消费的容器
     *
     * 创建 Redis 消息监听容器的一个 Spring Bean，在 Spring 容器启动时被初始化，并在容器销毁前被关闭
     * 其中，initMethod = "start" 表示 Bean 被初始化后，会执行 start 方法进行启动初始化
     *   destroyMethod = "stop" 表示 Bean 被销毁时，会执行 stop 方法进行关闭 这些都是${@link RedisMessageListenerContainer} 中的方法
     * @param redisMQTemplate 我自定义的redisMQTemplate对象
     * @return 返回一个 ${@link RedisMessageListenerContainer} 对象
     */
    @Bean(initMethod = "start",destroyMethod = "stop")
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisMQTemplate redisMQTemplate){
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory());

        return redisMessageListenerContainer;
    }

    /**
     *  创建 Redis Stream 集群消费的容器
     *  <p>
     *  Redis Stream 的 xreadgroup 命令：https://www.geek-book.com/src/docs/redis/redis/redis.io/commands/xreadgroup.html
     *  </p>
     *  给AbstractStreamMessageListener的原因就是为了{@link AbstractStreamMessageListener#onMessage(ObjectRecord)} ()}
     * @param redisMQTemplate 我自定义的redisMQTemplate对象
     * @param streamMessageListeners RedisStream 实现集群消费
     * @return
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
            RedisMQTemplate redisMQTemplate, List<AbstractStreamMessageListener<?>> streamMessageListeners) {
        RedisTemplate<String, ?> redisTemplate = redisMQTemplate.getRedisTemplate();
        checkRedisVersion(redisTemplate);
        // 第一步，创建 StreamMessageListenerContainer 容器
        // 创建 options 配置
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>>
                containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .batchSize(10) // 一次性最多拉取多少条消息
                .targetType(String.class) // 目标类型。统一使用 String，通过自己封装的 AbstractStreamMessageListener 去反序列化
                .build();
        // 创建 container 对象
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer =
                DefaultStreamMessageListenerContainerX
                .create(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory(), containerOptions);
        // 第二步，注册监听器，消费对应的 Stream 主题
        String consumerName = buildConsumerName();
        streamMessageListeners.parallelStream().forEach(listener -> {
            log.info("[redisStreamMessageListenerContainer][开始注册 StreamKey({}) 对应的监听器({})]",
                    listener.getStreamKey(), listener.getClass().getName());
            // 创建 listener 对应的消费者分组
            try {
                redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
            } catch (Exception ignore) {
            }
            // 设置 listener 对应的 redisTemplate
            listener.setRedisMQTemplate(redisMQTemplate);
            // 创建 Consumer 对象
            Consumer consumer = Consumer.from(listener.getGroup(), consumerName);
            // 设置 Consumer 消费进度，以最小消费进度为准
            StreamOffset<String> streamOffset = StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed());
            // 设置 Consumer 监听
            StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder = StreamMessageListenerContainer.StreamReadRequest
                    .builder(streamOffset).consumer(consumer)
                    .autoAcknowledge(false) // 不自动 ack
                    .cancelOnError(throwable -> false); // 默认配置，发生异常就取消消费，显然不符合预期；因此，我们设置为 false
            streamMessageListenerContainer.register(builder.build(), listener);
            log.info("[redisStreamMessageListenerContainer][完成注册 StreamKey({}) 对应的监听器({})]",
                    listener.getStreamKey(), listener.getClass().getName());
        });
        return streamMessageListenerContainer;
    }

    /**
     * 构建消费者名字，使用本地 IP + 进程编号的方式。
     * 参考自 RocketMQ clientId 的实现
     *
     * @return 消费者名字
     */
    private static String buildConsumerName() {
        return String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
    }

    /**
     * 校验 Redis 版本号，是否满足最低的版本号要求！
     */
    private static void checkRedisVersion(RedisTemplate<String, ?> redisTemplate) {
        // 获得 Redis 版本
        Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        String version = MapUtil.getStr(info, "redis_version");
        // 校验最低版本必须大于等于 5.0.0
        int majorVersion = Integer.parseInt(StrUtil.subBefore(version, '.', false));
        if (majorVersion < 5) {
            throw new IllegalStateException(StrUtil.format("您当前的 Redis 版本为 {}，小于最低要求的 5.0.0 版本！" +
                    "请参考 {} 文档进行安装。", version, DocumentEnum.REDIS_INSTALL.getUrl()));
        }
    }
}
