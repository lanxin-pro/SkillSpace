package cn.iocoder.educate.framework.quartz.core.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.iocoder.educate.framework.quartz.core.enums.JobDataKeyEnum;
import cn.iocoder.educate.framework.quartz.core.service.JobLogFrameworkService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * 基础 Job 调用者，负责调用 {@link JobHandler#execute(String)} 执行任务
 *
 * @DisallowConcurrentExecution：此注释用于确保一次只有一个作业实例在运行。如果作业已经在运行，
 *  Quartz 会等待其完成后再启动另一个实例。当您想要防止多个相同作业实例同时运行并可能干扰彼此时，这很有用。
 *
 * @PersistJobDataAfterExecution：此注释用于指示 Quartz 在其执行后持久化作业数据。
 *  作业数据包括您要与作业关联的任何数据，例如参数或配置设置。
 *  默认情况下，Quartz 会在每次执行后重置作业数据。
 *  但是，当您使用此注释时，Quartz 将保留作业数据以供下一次执行使用，允许您在随后的执行中访问它。
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 16:22
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Slf4j
public class JobHandlerInvoker extends QuartzJobBean {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private JobLogFrameworkService jobLogFrameworkService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 第一步，获得 Job 数据
        Long jobId = jobExecutionContext.getMergedJobDataMap()
                .getLong(JobDataKeyEnum.JOB_ID.name());

        // 控制器名称
        String jobHandlerName = jobExecutionContext.getMergedJobDataMap()
                .getString(JobDataKeyEnum.JOB_HANDLER_NAME.name());

        // 参数名称
        String jobHandlerParam = jobExecutionContext.getMergedJobDataMap()
                .getString(JobDataKeyEnum.JOB_HANDLER_PARAM.name());

        int refireCount  = jobExecutionContext.getRefireCount();

        int retryCount = (Integer) jobExecutionContext.getMergedJobDataMap()
                .getOrDefault(JobDataKeyEnum.JOB_RETRY_COUNT.name(), 0);

        int retryInterval = (Integer) jobExecutionContext.getMergedJobDataMap()
                .getOrDefault(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), 0);

        // 第二步，执行任务
        Long jobLogId = null;
        LocalDateTime startTime = LocalDateTime.now();
        String data = null;
        Throwable exception = null;

        try {
            // 记录 Job 日志（初始）
            jobLogId = jobLogFrameworkService.createJobLog(jobId, startTime, jobHandlerName,
                    jobHandlerParam, refireCount + 1);
            // 反射出bean，并且执行
            data = this.executeInternal(jobHandlerName, jobHandlerParam);
        } catch (Exception e) {
            exception = e;
        }

        // 第三步，记录执行日志
        this.updateJobLogResultAsync(jobLogId, startTime, data, exception, jobExecutionContext);

        // 第四步，处理有异常的情况
        handleException(exception, refireCount, retryCount, retryInterval);

    }

    private String executeInternal(String jobHandlerName, String jobHandlerParam) throws Exception {
        JobHandler bean = (JobHandler) applicationContext.getBean(jobHandlerName);
        log.info("当前JobHandler加载的对象{}",bean);
        // 获得 JobHandler 对象
        JobHandler jobHandler = applicationContext.getBean(jobHandlerName, JobHandler.class);
        Assert.notNull(jobHandler, "JobHandler 不会为空");
        // 执行任务
        return jobHandler.execute(jobHandlerParam);
    }

    private void updateJobLogResultAsync(Long jobLogId, LocalDateTime startTime, String data, Throwable exception,
                                         JobExecutionContext executionContext) {
        LocalDateTime endTime = LocalDateTime.now();
        // 处理是否成功
        boolean success = exception == null;
        if (!success) {
            // 错误 获取异常链中最尾端的异常的消息
            data = ExceptionUtil.getRootCauseMessage(exception);
        }
        // 更新日志
        try {
            jobLogFrameworkService.updateJobLogResultAsync(jobLogId, endTime,
                    (int) LocalDateTimeUtil.between(startTime, endTime).toMillis(), success, data);
        } catch (Exception ex) {
            log.error("[executeInternal][Job({}) logId({}) 记录执行日志失败({}/{})]",
                    executionContext.getJobDetail().getKey(), jobLogId, success, data);
        }
    }

    private void handleException(Throwable exception,
                                 int refireCount, int retryCount, int retryInterval) throws JobExecutionException {
        // TODO j-sentinel 这里可以考虑发邮件了

        // 如果有异常，则进行重试
        if (exception == null) {
            return;
        }
        // 情况一：如果到达重试上限，则直接抛出异常即可
        if (refireCount >= retryCount) {
            throw new JobExecutionException(exception);
        }

        // 情况二：如果未到达重试上限，则 sleep 一定间隔时间，然后重试
        // 这里使用 sleep 来实现，主要还是希望实现比较简单。因为，同一时间，不会存在大量失败的 Job。
        if (retryInterval > 0) {
            ThreadUtil.sleep(retryInterval);
        }
        // 第二个参数，refireImmediately = true，表示立即重试
        throw new JobExecutionException(exception, true);
    }

}
