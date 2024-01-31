package cn.iocoder.educate.module.course.convert.online;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseOnlineCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseOnlineRespVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseOnlineUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseOnlineUpdateStatusReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/28 12:49
 */
@Mapper
public interface CourseOnlineConvert {

    CourseOnlineConvert INSTANCE = Mappers.getMapper(CourseOnlineConvert.class);

    CourseOnlineRespVO convert(CourseChapterDO courseOnlineDO);

    List<CourseOnlineRespVO> convert(List<CourseChapterDO> courseOnlineDO);

    CourseChapterDO convert(CourseOnlineCreateReqVO courseOnlineDO);

    CourseChapterDO convert(CourseOnlineUpdateReqVO courseOnlineDO);

    CourseChapterDO convert(CourseOnlineUpdateStatusReqVO courseOnlineDO);

    PageResult<CourseOnlineRespVO> convertPage(PageResult<CourseChapterDO> courseOnlineDOPageResult);

}
