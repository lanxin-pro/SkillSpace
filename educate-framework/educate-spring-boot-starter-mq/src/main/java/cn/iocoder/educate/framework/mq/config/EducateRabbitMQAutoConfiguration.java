package cn.iocoder.educate.framework.mq.config;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

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
}
