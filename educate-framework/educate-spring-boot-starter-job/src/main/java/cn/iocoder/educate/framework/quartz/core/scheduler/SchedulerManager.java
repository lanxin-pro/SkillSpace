package cn.iocoder.educate.framework.quartz.core.scheduler;

import cn.iocoder.educate.framework.quartz.core.enums.JobDataKeyEnum;
import cn.iocoder.educate.framework.quartz.core.handler.JobHandlerInvoker;
import org.quartz.*;

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

    /**
     * 添加 Job 到 Quartz 中
     *
     * @param jobId 任务编号
     * @param jobHandlerName 任务处理器的名字
     * @param jobHandlerParam 任务处理器的参数
     * @param cronExpression CRON 表达式
     * @param retryCount 重试次数
     * @param retryInterval 重试间隔
     * @throws SchedulerException 添加异常
     */
    public void addJob(Long jobId, String jobHandlerName, String jobHandlerParam, String cronExpression,
                       Integer retryCount, Integer retryInterval)
            throws SchedulerException {
        // 创建 JobDetail 对象
        JobDetail jobDetail = JobBuilder.newJob(JobHandlerInvoker.class)
                .usingJobData(JobDataKeyEnum.JOB_ID.name(), jobId)
                .usingJobData(JobDataKeyEnum.JOB_HANDLER_NAME.name(), jobHandlerName)
                .withIdentity(jobHandlerName)
                .build();
        // 创建 Trigger 对象
        Trigger trigger = this.buildTrigger(jobHandlerName, jobHandlerParam, cronExpression, retryCount, retryInterval);
        // 新增调度
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private Trigger buildTrigger(String jobHandlerName, String jobHandlerParam, String cronExpression,
                                 Integer retryCount, Integer retryInterval) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobHandlerName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(JobDataKeyEnum.JOB_HANDLER_PARAM.name(), jobHandlerParam)
                .usingJobData(JobDataKeyEnum.JOB_RETRY_COUNT.name(), retryCount)
                .usingJobData(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), retryInterval)
                .build();
    }

}
