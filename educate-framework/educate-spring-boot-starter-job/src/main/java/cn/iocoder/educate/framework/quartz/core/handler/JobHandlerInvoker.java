package cn.iocoder.educate.framework.quartz.core.handler;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.quartz.core.enums.JobDataKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

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

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 第一步，获得 Job 数据
        Long jobId = jobExecutionContext.getMergedJobDataMap().getLong(JobDataKeyEnum.JOB_ID.name());
        String jobHandlerName = jobExecutionContext.getMergedJobDataMap().getString(JobDataKeyEnum.JOB_HANDLER_NAME.name());
        String jobHandlerParam = jobExecutionContext.getMergedJobDataMap().getString(JobDataKeyEnum.JOB_HANDLER_PARAM.name());
        int refireCount  = jobExecutionContext.getRefireCount();
        int retryCount = (Integer) jobExecutionContext.getMergedJobDataMap().getOrDefault(JobDataKeyEnum.JOB_RETRY_COUNT.name(), 0);
        int retryInterval = (Integer) jobExecutionContext.getMergedJobDataMap().getOrDefault(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), 0);
        // 执行任务
        try {
            this.executeInternal(jobHandlerName, jobHandlerParam);
        } catch (Exception e) {
            log.error("出大事了!!!!!!!!!!!!!!");
            e.printStackTrace();
        }
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

}
