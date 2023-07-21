package cn.iocoder.educate.module.system.mq.producer.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/7/20 13:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "system.menu.refresh";
    }

}
