package cn.iocoder.educate.module.system.mq.message.sensitiveword;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 敏感词的刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/21 8:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SensitiveWordRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.sensitive-word.refresh";
    }

}
