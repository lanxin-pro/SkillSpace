package cn.iocoder.educate.module.system.mq.message.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/10 10:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.role.refresh";
    }

}
