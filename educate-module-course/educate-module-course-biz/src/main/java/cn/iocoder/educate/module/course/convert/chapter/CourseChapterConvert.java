package cn.iocoder.educate.module.course.convert.chapter;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseRespVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseUpdateStatusReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.course.CourseDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/28 12:49
 */
@Mapper
public interface CourseChapterConvert {

    CourseChapterConvert INSTANCE = Mappers.getMapper(CourseChapterConvert.class);

    CourseRespVO convert(CourseDO courseOnlineDO);

    List<CourseRespVO> convert(List<CourseDO> courseOnlineDO);

    CourseDO convert(CourseCreateReqVO courseOnlineDO);

    CourseDO convert(CourseUpdateReqVO courseOnlineDO);

    CourseDO convert(CourseUpdateStatusReqVO courseOnlineDO);

    PageResult<CourseRespVO> convertPage(PageResult<CourseDO> courseOnlineDOPageResult);

}
