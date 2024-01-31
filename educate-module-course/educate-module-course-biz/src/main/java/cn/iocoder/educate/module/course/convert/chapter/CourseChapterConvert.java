package cn.iocoder.educate.module.course.convert.chapter;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterUpdateStatusReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
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

    CourseChapterRespVO convert(CourseChapterDO courseOnlineDO);

    List<CourseChapterRespVO> convert(List<CourseChapterDO> courseOnlineDO);

    CourseChapterDO convert(CourseChapterCreateReqVO courseOnlineDO);

    CourseChapterDO convert(CourseChapterUpdateReqVO courseOnlineDO);

    CourseChapterDO convert(CourseChapterUpdateStatusReqVO courseOnlineDO);

    PageResult<CourseChapterRespVO> convertPage(PageResult<CourseChapterDO> courseOnlineDOPageResult);

}
