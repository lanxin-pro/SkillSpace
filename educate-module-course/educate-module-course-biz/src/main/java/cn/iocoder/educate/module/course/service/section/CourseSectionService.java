package cn.iocoder.educate.module.course.service.section;

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
    List<CourseSectionDO> findCourseSectionList(String courseId);

}
