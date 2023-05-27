package cn.iocoder.educate.framework.mq.stream;

/**
 * Redis Stream Message 抽象类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/26 12:40
 */
public abstract class AbstractStreamMessage {

    /**
     * 获得 Redis Stream Key
     *
     * @return
     */
    public abstract String getStreamKey();
}
