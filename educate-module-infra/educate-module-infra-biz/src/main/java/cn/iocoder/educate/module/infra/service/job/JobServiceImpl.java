package cn.iocoder.educate.module.infra.service.job;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.quartz.core.scheduler.SchedulerManager;
import cn.iocoder.educate.framework.quartz.core.util.CronUtils;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobUpdateReqVO;
import cn.iocoder.educate.module.infra.covert.job.JobConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.dal.mysql.job.JobMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.infra.enums.job.JobStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobUpdateReqVO updateReqVO) throws SchedulerException {
        validateCronExpression(updateReqVO.getCronExpression());
        // 校验存在
        JobDO job = validateJobExists(updateReqVO.getId());
        // 只有开启状态，才可以修改.原因是，如果出暂停状态，修改 Quartz Job 时，会导致任务又开始执行
        if (!job.getStatus().equals(JobStatusEnum.NORMAL.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_UPDATE_ONLY_NORMAL_STATUS);
        }
        // 更新
        JobDO jobDO = JobConvert.INSTANCE.convert(updateReqVO);
        // 监控超时时间 为空就为0
        fillJobMonitorTimeoutEmpty(jobDO);
        jobMapper.updateById(jobDO);

        // 更新 Job 到 Quartz 中
        schedulerManager.updateJob(job.getHandlerName(), updateReqVO.getHandlerParam(), updateReqVO.getCronExpression(),
                updateReqVO.getRetryCount(), updateReqVO.getRetryInterval());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobStatus(Long id, Integer status) throws SchedulerException {
        // 校验 status
        if (!Arrays.asList(JobStatusEnum.NORMAL.getStatus(),JobStatusEnum.STOP.getStatus()).contains(status)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_CHANGE_STATUS_INVALID);
        }
        // 校验存在
        JobDO job = validateJobExists(id);
        // 校验是否已经为当前状态
        if (job.getStatus().equals(status)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_CHANGE_STATUS_EQUALS);
        }
        // 更新 Job 状态
        JobDO jobDO = JobDO
                .builder()
                .id(id)
                .status(status)
                .build();
        jobMapper.updateById(jobDO);
        // 更新状态 Job 到 Quartz 中
        // 开启
        if (JobStatusEnum.NORMAL.getStatus().equals(status)) {
            schedulerManager.resumeJob(job.getHandlerName());
        } else { // 暂停
            schedulerManager.pauseJob(job.getHandlerName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Long id) throws SchedulerException {
        // 校验存在
        JobDO job = validateJobExists(id);
        // 更新
        jobMapper.deleteById(id);

        // 删除 Job 到 Quartz 中
        schedulerManager.deleteJob(job.getHandlerName());
    }

    @Override
    public void triggerJob(Long id) throws SchedulerException {
        // 校验存在
        JobDO job = validateJobExists(id);

        // 触发 Quartz 中的 Job
        schedulerManager.triggerJob(job.getId(), job.getHandlerName(), job.getHandlerParam());
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

    private JobDO validateJobExists(Long id) {
        JobDO job = jobMapper.selectById(id);
        if (job == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.JOB_NOT_EXISTS);
        }
        return job;
    }

}
