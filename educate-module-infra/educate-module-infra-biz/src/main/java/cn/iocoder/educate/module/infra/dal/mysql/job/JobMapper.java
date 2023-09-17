package cn.iocoder.educate.module.infra.dal.mysql.job;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * 定时任务 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:49
 */
@Mapper
public interface JobMapper extends BaseMapper<JobDO> {

    default PageResult<JobDO> selectPage(JobPageReqVO jobPageReqVO) {
        LambdaQueryWrapper<JobDO> jobDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobDOLambdaQueryWrapper
                .like(StringUtils.hasText(jobPageReqVO.getName()),
                        JobDO::getName,jobPageReqVO.getName())
                .like(StringUtils.hasText(jobPageReqVO.getHandlerName()),
                        JobDO::getHandlerName,jobPageReqVO.getHandlerName())
                .eq(ObjectUtil.isNotEmpty(jobPageReqVO.getStatus()),
                        JobDO::getStatus,jobPageReqVO.getStatus());
        Page<JobDO> page = new Page<>(jobPageReqVO.getPageNo(), jobPageReqVO.getPageSize());
        Page<JobDO> jobDOPage = this.selectPage(page, jobDOLambdaQueryWrapper);
        return new PageResult<>(jobDOPage.getRecords(),jobDOPage.getTotal());
    }

    default JobDO selectByHandlerName(String handlerName) {
        LambdaQueryWrapper<JobDO> jobDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobDOLambdaQueryWrapper.eq(JobDO::getHandlerName, handlerName);
        return this.selectOne(jobDOLambdaQueryWrapper);
    }

}
