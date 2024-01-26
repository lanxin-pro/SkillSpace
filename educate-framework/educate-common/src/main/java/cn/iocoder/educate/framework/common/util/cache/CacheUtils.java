package cn.iocoder.educate.framework.common.util.cache;

import com.google.common.cache.*;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Cache 工具类
 *
 * @author j-sentinel
 * @date 2024/1/25 19:00
 */
public class CacheUtils {

    public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,  // 设置核心线程数
                10, // 设置最大线程数
                60, // 设置线程空闲时间（秒）
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
        return CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(8, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                    }
                })
                // 只阻塞当前数据加载线程，其他线程返回旧值
                .refreshAfterWrite(duration)
                // 通过 asyncReloading 实现全异步加载，包括 refreshAfterWrite 被阻塞的加载线程
                .build(CacheLoader.asyncReloading(loader, executor));
    }

}
