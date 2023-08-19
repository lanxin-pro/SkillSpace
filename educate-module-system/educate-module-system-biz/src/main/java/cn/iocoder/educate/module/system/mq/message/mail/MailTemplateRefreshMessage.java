package cn.iocoder.educate.module.system.mq.message.mail;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱模板的数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 13:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailTemplateRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.mail-template.refresh";
    }

}
