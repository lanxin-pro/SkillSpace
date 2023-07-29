package cn.iocoder.educate.module.system.mq.consumer.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.permission.UserRoleRefreshMessage;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/29 13:39
 */
@Component
@Slf4j
public class UserRoleRefreshConsumer extends AbstractChannelMessageListener<UserRoleRefreshMessage> {

    @Resource
    private PermissionService permissionService;

    @Override
    public void onMessage(UserRoleRefreshMessage message) {
        log.info("[onMessage][收到 User 与 Role 的关联刷新消息]");
        permissionService.initLocalCache();
    }

}
