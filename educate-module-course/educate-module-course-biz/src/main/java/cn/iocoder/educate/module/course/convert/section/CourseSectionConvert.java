package cn.iocoder.educate.module.course.convert.section;

import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionRespVO;
import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;
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

    CourseSectionRespVO convert(CourseSectionDO courseSectionDO);

    List<CourseSectionRespVO> convert(List<CourseSectionDO> chapterList);

}
