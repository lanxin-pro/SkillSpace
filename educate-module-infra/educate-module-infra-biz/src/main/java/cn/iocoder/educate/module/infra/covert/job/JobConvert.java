package cn.iocoder.educate.module.infra.covert.job;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 定时任务 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:51
 */
@Mapper
public interface JobConvert {

    JobConvert INSTANCE = Mappers.getMapper(JobConvert.class);

    JobDO convert(JobCreateReqVO bean);

    JobRespVO convert(JobDO bean);

    PageResult<JobRespVO> convertPage(PageResult<JobDO> page);

}
