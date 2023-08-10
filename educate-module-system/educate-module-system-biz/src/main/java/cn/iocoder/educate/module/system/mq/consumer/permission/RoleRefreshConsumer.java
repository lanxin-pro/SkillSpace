package cn.iocoder.educate.module.system.mq.consumer.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.message.permission.RoleRefreshMessage;
import cn.iocoder.educate.module.system.service.permission.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link RoleRefreshMessage} 的消费者
 *
 * @Author: j-sentinel
 * @Date: 2023/8/10 10:26
 */
@Component
@Slf4j
public class RoleRefreshConsumer extends AbstractChannelMessageListener<RoleRefreshMessage> {

    @Resource
    private RoleService roleService;

    @Override
    public void onMessage(RoleRefreshMessage message) {
        log.info("[onMessage][收到 Role 刷新消息]");
        roleService.initLocalCache();
    }

}
