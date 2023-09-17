package cn.iocoder.educate.module.infra.service.job;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.quartz.core.scheduler.SchedulerManager;
import cn.iocoder.educate.framework.quartz.core.util.CronUtils;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.covert.job.JobConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.dal.mysql.job.JobMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.infra.enums.job.JobStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 定时任务 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:48
 */
@Service
@Validated
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;

    @Resource
    private SchedulerManager schedulerManager;

    @Override
    public PageResult<JobDO> getJobPage(JobPageReqVO pageReqVO) {
        return jobMapper.selectPage(pageReqVO);
    }

    @Override
    public JobDO getJob(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    public Long createJob(JobCreateReqVO createReqVO) throws SchedulerException {
        // 校验cron是否正确
        validateCronExpression(createReqVO.getCronExpression());
        // 校验唯一性
        if (jobMapper.selectByHandlerName(createReqVO.getHandlerName()) != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_HANDLER_EXISTS);
        }
        // 插入
        JobDO job = JobConvert.INSTANCE.convert(createReqVO);
        job.setStatus(JobStatusEnum.INIT.getStatus());
        // 监控超时时间 为空就为0
        fillJobMonitorTimeoutEmpty(job);
        jobMapper.insert(job);

        // 添加 Job 到 Quartz 中
        schedulerManager.addJob(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getCronExpression(),
                createReqVO.getRetryCount(), createReqVO.getRetryInterval());
        // 更新
        JobDO jobDO = JobDO.builder()
                .id(job.getId())
                .status(JobStatusEnum.NORMAL.getStatus())
                .build();
        jobMapper.updateById(jobDO);

        // 返回
        return job.getId();
    }

    private void validateCronExpression(String cronExpression) {
        if (!CronUtils.isValid(cronExpression)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_CRON_EXPRESSION_VALID);
        }
    }

    private static void fillJobMonitorTimeoutEmpty(JobDO job) {
        if (job.getMonitorTimeout() == null) {
            job.setMonitorTimeout(0);
        }
    }

}
