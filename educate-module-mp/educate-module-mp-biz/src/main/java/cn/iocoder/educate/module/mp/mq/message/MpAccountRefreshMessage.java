package cn.iocoder.educate.module.mp.mq.message;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公众号账号刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/9/1 11:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MpAccountRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "mp.account.refresh";
    }

}
