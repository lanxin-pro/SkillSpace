package cn.iocoder.educate.framework.errorcode.core.generator;

import cn.iocoder.educate.module.system.api.errorcode.ErrorCodeApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * ErrorCodeAutoGenerator 的实现类
 * 目的是，扫描指定的 {@link #constantsClassList} 类，写入到 system 服务中
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 12:17
 */
@Slf4j
@RequiredArgsConstructor
public class ErrorCodeAutoGeneratorImpl implements ErrorCodeAutoGenerator {

    /**
     * 应用分组
     */
    private final String applicationName;

    /**
     * 错误码枚举类
     */
    private final List<String> constantsClassList;

    /**
     * 错误码 Api
     */
    private final ErrorCodeApi errorCodeApi;

    /**
     * @EventListener(ApplicationReadyEvent.class) 它用于在应用程序启动完成后执行相应的事件处理逻辑
     *
     * 具体来说，@EventListener 注解标记的方法将在 ApplicationReadyEvent 事件发生时被调用。
     * ApplicationReadyEvent 是Spring框架中的一个事件，表示应用程序已经准备好接受请求，并且所有的初始化工作都已完成。
     */
    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Async // 异步，保证项目的启动过程，毕竟非关键流程
    public void execute() {
        log.info("[execute][写入到 system 组件完成]");
    }
}
