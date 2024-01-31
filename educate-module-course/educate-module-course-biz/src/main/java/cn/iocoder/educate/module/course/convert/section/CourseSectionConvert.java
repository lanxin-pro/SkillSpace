package cn.iocoder.educate.module.course.convert.section;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author j-sentinel
 * @date 2024/1/28 12:49
 */
@Mapper
public interface CourseSectionConvert {

    CourseSectionConvert INSTANCE = Mappers.getMapper(CourseSectionConvert.class);

}
