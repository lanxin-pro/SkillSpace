package cn.iocoder.educate.module.system.mq.message.auth;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/7 11:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2ClientRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.oauth2-client.refresh";
    }

}
