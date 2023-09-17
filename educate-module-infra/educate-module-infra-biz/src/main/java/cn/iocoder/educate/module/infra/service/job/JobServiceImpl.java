package cn.iocoder.educate.module.infra.service.job;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.dal.mysql.job.JobMapper;
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
        return null;
    }

}
