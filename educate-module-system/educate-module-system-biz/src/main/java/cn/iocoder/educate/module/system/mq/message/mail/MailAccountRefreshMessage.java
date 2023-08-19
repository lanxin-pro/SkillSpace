package cn.iocoder.educate.module.system.mq.message.mail;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱账号的数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 9:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailAccountRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.mail-account.refresh";
    }

}
