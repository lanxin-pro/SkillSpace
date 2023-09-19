package cn.iocoder.educate.module.infra.dal.mysql.job;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.log.JobLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobLogDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * 任务日志 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/9/19 13:40
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLogDO> {

    default PageResult<JobLogDO> selectPage(JobLogPageReqVO jobLogPageReqVO) {
        LambdaQueryWrapper<JobLogDO> jobDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobDOLambdaQueryWrapper
                .like(StringUtils.hasText(jobLogPageReqVO.getHandlerName()),
                        JobLogDO::getHandlerName,jobLogPageReqVO.getHandlerName())
                .eq(ObjectUtil.isNotEmpty(jobLogPageReqVO.getJobId()),
                        JobLogDO::getJobId,jobLogPageReqVO.getJobId())
                .eq(ObjectUtil.isNotEmpty(jobLogPageReqVO.getStatus()),
                        JobLogDO::getStatus,jobLogPageReqVO.getStatus())
                .ge(jobLogPageReqVO.getBeginTime() != null,
                        JobLogDO::getBeginTime,jobLogPageReqVO.getBeginTime())
                .le(jobLogPageReqVO.getEndTime() != null,
                        JobLogDO::getEndTime,jobLogPageReqVO.getEndTime())
                .orderByDesc(JobLogDO::getId);
        Page<JobLogDO> page = new Page<>(jobLogPageReqVO.getPageNo(), jobLogPageReqVO.getPageSize());
        Page<JobLogDO> jobLogDOPage = this.selectPage(page, jobDOLambdaQueryWrapper);
        return new PageResult<>(jobLogDOPage.getRecords(),jobLogDOPage.getTotal());
    }

}
