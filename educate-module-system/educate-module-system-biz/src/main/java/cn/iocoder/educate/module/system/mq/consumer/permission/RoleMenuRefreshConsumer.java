package cn.iocoder.educate.module.system.mq.consumer.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.permission.RoleMenuRefreshMessage;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/10 10:58
 */
@Component
@Slf4j
public class RoleMenuRefreshConsumer extends AbstractChannelMessageListener<RoleMenuRefreshMessage> {

    @Resource
    private PermissionService permissionService;

    @Override
    public void onMessage(RoleMenuRefreshMessage message) {
        log.info("[onMessage][收到 Role 与 Menu 的关联刷新消息]");
        permissionService.initLocalCache();
    }

}
