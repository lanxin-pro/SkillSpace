package cn.iocoder.educate.module.course.service.section;

import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionReqVO;
import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionRespVO;
import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:49
 */
public interface CourseSectionService {

    /**
     * 根据课程查询对应的章信息
     *
     * @param courseId 课程id
     * @return 章信息
     */
    List<CourseSectionRespVO> findCourseChapterList(String courseId);

    /**
     * 根据章ID查询对应节信息
     *
     * @param id 课程的章id
     * @return
     */
    List<CourseSectionRespVO> findCourseSectionList(Long id);

    /**
     * 保存章信息
     *
     * @param courseSectionReqVO 章信息
     * @return 保存的章信息
     */
    CourseSectionDO saveUpdateChapterLesson(CourseSectionReqVO courseSectionReqVO);


    CourseSectionDO getChapterLessons(Long opid);
}
