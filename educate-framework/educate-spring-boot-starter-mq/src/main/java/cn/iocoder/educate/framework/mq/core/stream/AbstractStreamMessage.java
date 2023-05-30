package cn.iocoder.educate.framework.mq.core.stream;

import cn.iocoder.educate.framework.mq.core.message.AbstractRedisMessage;

/**
 * Redis Stream Message 抽象类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/26 12:40
 */
public abstract class AbstractStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key
     *
     * @return
     */
    public abstract String getStreamKey();
}
