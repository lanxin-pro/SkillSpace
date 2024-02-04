package cn.iocoder.educate.module.course.service.section;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionReqVO;
import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionRespVO;
import cn.iocoder.educate.module.course.convert.section.CourseSectionConvert;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;
import cn.iocoder.educate.module.course.dal.mysql.section.CourseSectionMapper;
import cn.iocoder.educate.module.course.service.chapter.CourseChapterService;
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
public class CourseSectionServiceImpl implements CourseSectionService {

    @Resource
    private CourseSectionMapper courseSectionMapper;

    @Resource
    private CourseChapterService courseChapterService;

    @Override
    public List<CourseSectionRespVO> findCourseChapterList(String courseId) {
        List<CourseSectionDO> courseChapterList = courseSectionMapper.findCourseChapterList(courseId);
        return CourseSectionConvert.INSTANCE.convert(courseChapterList);
    }

    @Override
    public List<CourseSectionRespVO> findCourseSectionList(Long id) {
        List<CourseSectionDO> courseSectionList = courseSectionMapper.findCourseSectionList(id);
        return CourseSectionConvert.INSTANCE.convert(courseSectionList);
    }

    @Override
    public CourseSectionDO saveUpdateChapterLesson(CourseSectionReqVO courseSectionReqVO) {
        validateSaveOrUpdateCourse(courseSectionReqVO);
        CourseSectionDO courseSectionDO = CourseSectionConvert.INSTANCE.convert(courseSectionReqVO);
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
    public CourseSectionDO getChapterLessons(Long opid) {
        // 校验存在
        return validateChapterLessonsExists(opid);
    }

    private void validateSaveOrUpdateCourse(CourseSectionReqVO courseSectionReqVO) {
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

    private CourseSectionDO validateChapterLessonsExists(Long opid) {
        CourseSectionDO courseSectionDO = courseSectionMapper.selectById(opid);
        if (courseSectionDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COURSE_CHAPTER_NOT_EXISTS);
        }
        return courseSectionDO;
    }

}
