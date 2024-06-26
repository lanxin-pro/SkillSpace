package cn.iocoder.educate.module.system.mq.producer.permission;

import cn.iocoder.educate.framework.mq.core.RedisMQTemplate;
import cn.iocoder.educate.module.system.mq.message.permission.RoleMenuRefreshMessage;
import cn.iocoder.educate.module.system.mq.message.permission.UserRoleRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Permission 权限相关消息的 Producer
 *
 * @Author: j-sentinel
 * @Date: 2023/7/29 13:37
 */
@Component
public class PermissionProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link UserRoleRefreshMessage} 消息
     */
    public void sendUserRoleRefreshMessage() {
        UserRoleRefreshMessage message = new UserRoleRefreshMessage();
        redisMQTemplate.send(message);
    }

    /**
     * 发送 {@link RoleMenuRefreshMessage} 消息
     */
    public void sendRoleMenuRefreshMessage() {
        RoleMenuRefreshMessage message = new RoleMenuRefreshMessage();
        redisMQTemplate.send(message);
    }
}
