package cn.iocoder.educate.module.system.mq.consumer.permission;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessageListener;
import cn.iocoder.educate.module.system.mq.producer.permission.MenuRefreshMessage;
import cn.iocoder.educate.module.system.service.permission.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/20 13:08
 *
 * 针对 {@link MenuRefreshMessage} 的消费者
 *
 */
@Component
@Slf4j
public class MenuRefreshConsumer extends AbstractChannelMessageListener<MenuRefreshMessage> {

    @Resource
    private MenuService menuService;

    @Override
    public void onMessage(MenuRefreshMessage message) {
        log.info("[onMessage][收到 Menu 刷新消息]");
        menuService.initLocalCache();
    }
}
