package cn.iocoder.educate.module.course.convert.online;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineRespVO;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author j-sentinel
 * @date 2024/1/28 12:49
 */
@Mapper
public interface CourseOnlineConvert {

    CourseOnlineConvert INSTANCE = Mappers.getMapper(CourseOnlineConvert.class);

    CourseOnlineRespVO convert(CourseOnlineDO courseOnlineDO);

    CourseOnlineDO convert(CourseOnlineCreateReqVO courseOnlineDO);

    PageResult<CourseOnlineRespVO> convertPage(PageResult<CourseOnlineDO> courseOnlineDOPageResult);

}
