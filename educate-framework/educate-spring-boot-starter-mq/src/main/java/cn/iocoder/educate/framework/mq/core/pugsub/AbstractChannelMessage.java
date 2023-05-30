package cn.iocoder.educate.framework.mq.core.pugsub;

import cn.iocoder.educate.framework.mq.core.message.AbstractRedisMessage;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/30 13:29
 */
public abstract class AbstractChannelMessage extends AbstractRedisMessage {

    public abstract String getChannel();
}
