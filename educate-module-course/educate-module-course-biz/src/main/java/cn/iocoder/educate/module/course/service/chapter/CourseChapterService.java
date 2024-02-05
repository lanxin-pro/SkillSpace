package cn.iocoder.educate.module.course.service.chapter;

import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:49
 */
public interface CourseChapterService {

    /**
     * 根据课程查询对应的章信息
     *
     * @param courseId 课程id
     * @return 章信息
     */
    List<CourseChapterRespVO> findCourseChapterList(String courseId);

    /**
     * 根据章ID查询对应节信息
     *
     * @param id 课程的章id
     * @return
     */
    List<CourseChapterRespVO> findCourseSectionList(Long id);

    /**
     * 保存章信息
     *
     * @param courseSectionReqVO 章信息
     * @return 保存的章信息
     */
    CourseChapterDO saveUpdateChapterLesson(CourseChapterReqVO courseSectionReqVO);

    /**
     * 查新章的信息
     * @param opid
     * @return
     */
    CourseChapterDO getChapterLessons(Long opid);


    /**
     * 根据章ID查询对应节信息
     *
     * @param chapterId
     * @return
     */
    List<CourseChapterDO> findSectionList(Long chapterId);
}
