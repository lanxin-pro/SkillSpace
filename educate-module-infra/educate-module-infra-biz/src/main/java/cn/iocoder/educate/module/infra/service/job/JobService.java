package cn.iocoder.educate.module.infra.service.job;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import org.quartz.SchedulerException;

import javax.validation.Valid;

/**
 * 定时任务 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:48
 */
public interface JobService {

    /**
     * 获得定时任务分页
     *
     * @param pageReqVO 分页查询
     * @return 定时任务分页
     */
    PageResult<JobDO> getJobPage(JobPageReqVO pageReqVO);

    /**
     * 获得定时任务
     *
     * @param id 编号
     * @return 定时任务
     */
    JobDO getJob(Long id);

    /**
     * 创建定时任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createJob(@Valid JobCreateReqVO createReqVO) throws SchedulerException;

}
