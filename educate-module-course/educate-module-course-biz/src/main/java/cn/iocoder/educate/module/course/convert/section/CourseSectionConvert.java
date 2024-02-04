package cn.iocoder.educate.module.course.convert.section;

import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/28 12:49
 */
@Mapper
public interface CourseSectionConvert {

    CourseSectionConvert INSTANCE = Mappers.getMapper(CourseSectionConvert.class);

    CourseChapterRespVO convert(CourseChapterDO courseSectionDO);

    CourseChapterDO convert(CourseChapterReqVO courseSectionDO);

    List<CourseChapterRespVO> convert(List<CourseChapterDO> chapterList);

}
