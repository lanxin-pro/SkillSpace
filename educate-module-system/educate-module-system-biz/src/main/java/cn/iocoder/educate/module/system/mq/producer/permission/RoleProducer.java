package cn.iocoder.educate.module.system.mq.producer.permission;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import cn.iocoder.educate.module.system.mq.message.permission.RoleRefreshMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 角色与菜单数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/10 10:24
 */
@Component
public class RoleProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link RoleRefreshMessage} 消息
     */
    public void sendRoleRefreshMessage() {
        RoleRefreshMessage message = new RoleRefreshMessage();
        redisMQTemplate.send(message);
    }

}
