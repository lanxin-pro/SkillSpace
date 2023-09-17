package cn.iocoder.educate.framework.quartz.core.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

/**
 * {@link org.quartz.Scheduler} 的管理器，负责创建任务
 *
 * 考虑到实现的简洁性，我们使用 jobHandlerName 作为唯一标识，即：
 * 1. Job 的 {@link JobDetail#getKey()}
 * 2. Trigger 的 {@link Trigger#getKey()}
 *
 * 另外，jobHandlerName 对应到 Spring Bean 的名字，直接调用
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:29
 */
public class SchedulerManager {

    private final Scheduler scheduler;

    public SchedulerManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
