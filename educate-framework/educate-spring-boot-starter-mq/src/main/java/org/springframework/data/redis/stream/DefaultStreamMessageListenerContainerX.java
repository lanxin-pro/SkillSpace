package org.springframework.data.redis.stream;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.util.Assert;

/**
 * <p>
 *  DefaultStreamMessageListenerContainer 这个类不是 public 是因为它的设计初衷是作为 Spring Data Redis 的内部实现类使用。
 *  在 Spring Data Redis 中，AbstractMessageListenerContainer 类是一个抽象基类，
 *  DefaultMessageListenerContainer 和 DefaultStreamMessageListenerContainer 是其具体子类实现。
 *
 *
 *  其中，DefaultMessageListenerContainer 用于处理 Redis 普通的 Pub/Sub 订阅模式
 *  而 ${@link DefaultStreamMessageListenerContainer} 则用于处理 Redis 新的 Stream 数据结构。
 *
 *  DefaultStreamMessageListenerContainer 类是 package-private，也就是只能在其所在的包中访问
 *  这是为了避免开发人员直接使用该类而不是通过 Spring Data Redis 提供的高层次 API 进行操作 Redis Stream。
 *  因此，如果你想要使用 DefaultStreamMessageListenerContainer，建议使用 Spring Data Redis 提供的 Redis Stream 支持API。
 *
 *  需要注意的是，虽然 DefaultStreamMessageListenerContainer 类不是 public
 *  但是它的所有方法都是 public 的，因此在 Spring Data Redis 中任何一个类都是可以访问到这些方法的。
 *
 *  拓展 DefaultStreamMessageListenerContainer 实现，解决 Spring Data Redis + Redisson 结合使用时
 *  Redisson 在 Stream 获得不到数据时，返回 null 而不是空 List，导致 NPE 异常。
 *
 *  对应 issue：
 *  https://github.com/spring-projects/spring-data-redis/issues/2147 和 https://github.com/redisson/redisson/issues/4006
 *  目前看下来 Spring Data Redis 不肯加 null 判断，Redisson 暂时也没改返回 null 到空 List 的打算，所以暂时只能自己改，哽咽！
 * </p>
 *
 * @Author: j-sentinel
 * @Date: 2023/5/30 16:39
 */
public class DefaultStreamMessageListenerContainerX<K, V extends Record<K, ?>>
        extends DefaultStreamMessageListenerContainer<K, V> {

    /**
     * Create a new {@link DefaultStreamMessageListenerContainer}.
     *
     * @param connectionFactory must not be {@literal null}.
     * @param containerOptions  must not be {@literal null}.
     */
    DefaultStreamMessageListenerContainerX(RedisConnectionFactory connectionFactory, StreamMessageListenerContainerOptions<K, V> containerOptions) {
        super(connectionFactory, containerOptions);
    }

    /**
     * 参考 {@link StreamMessageListenerContainer#create(RedisConnectionFactory, StreamMessageListenerContainerOptions)} 的实现
     */
    public static <K, V extends Record<K, ?>> StreamMessageListenerContainer<K, V> create(RedisConnectionFactory connectionFactory, StreamMessageListenerContainer.StreamMessageListenerContainerOptions<K, V> options) {
        Assert.notNull(connectionFactory, "RedisConnectionFactory must not be null!");
        Assert.notNull(options, "StreamMessageListenerContainerOptions must not be null!");
        return new DefaultStreamMessageListenerContainerX<>(connectionFactory, options);
    }
}
