package cn.iocoder.educate.module.course.service.chapter;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.convert.section.CourseSectionConvert;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import cn.iocoder.educate.module.course.dal.mysql.chapter.CourseChapterMapper;
import cn.iocoder.educate.module.course.service.course.CourseService;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:49
 */
@Slf4j
@Service
public class CourseChapterServiceImpl implements CourseChapterService {

    @Resource
    private CourseChapterMapper courseSectionMapper;

    @Resource
    private CourseService courseChapterService;

    @Override
    public List<CourseChapterRespVO> findCourseChapterList(String courseId) {
        List<CourseChapterDO> courseChapterList = courseSectionMapper.findCourseChapterList(courseId);
        return CourseSectionConvert.INSTANCE.convert(courseChapterList);
    }

    @Override
    public List<CourseChapterRespVO> findCourseSectionList(Long id) {
        List<CourseChapterDO> courseSectionList = courseSectionMapper.findCourseSectionList(id);
        return CourseSectionConvert.INSTANCE.convert(courseSectionList);
    }

    @Override
    public CourseChapterDO saveUpdateChapterLesson(CourseChapterReqVO courseSectionReqVO) {
        validateSaveOrUpdateCourse(courseSectionReqVO);
        CourseChapterDO courseSectionDO = CourseSectionConvert.INSTANCE.convert(courseSectionReqVO);
        int operation;
        if(ObjectUtil.isEmpty(courseSectionReqVO.getId())) {
            operation = courseSectionMapper.insert(courseSectionDO);
        } else {
            operation = courseSectionMapper.updateById(courseSectionDO);
        }
        boolean flag = SqlHelper.retBool(operation);
        return flag ? courseSectionDO : null;
    }

    @Override
    public CourseChapterDO getChapterLessons(Long opid) {
        // 校验存在
        return validateChapterLessonsExists(opid);
    }

    @Override
    public List<CourseChapterDO> findSectionList(Long chapterId) {
        validateChapterLessonsExists(chapterId);
        return courseSectionMapper.findLessonList(chapterId);
    }

    private void validateSaveOrUpdateCourse(CourseChapterReqVO courseSectionReqVO) {
        // 指派的course必须存在
        validateCourseExists(courseSectionReqVO.getCourseId());

    }

    private void validateCourseExists(Long courseId) {
        if(ObjectUtil.isEmpty(courseId)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COURSE_CHAPTER_ERROR);
        }
        // 这里面也会又校验逻辑，复用？
        courseChapterService.getOnlineInfo(courseId);
    }

    private CourseChapterDO validateChapterLessonsExists(Long opid) {
        CourseChapterDO courseSectionDO = courseSectionMapper.selectById(opid);
        if (courseSectionDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COURSE_CHAPTER_NOT_EXISTS);
        }
        return courseSectionDO;
    }

}
