package cn.iocoder.educate.module.course.service.course;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CoursePageReqVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CourseUpdateStatusReqVO;
import cn.iocoder.educate.module.course.convert.chapter.CourseChapterConvert;
import cn.iocoder.educate.module.course.dal.dataobject.course.CourseDO;
import cn.iocoder.educate.module.course.dal.mysql.course.CourseMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/1/28 11:46
 */
@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public PageResult<CourseDO> getCourseOnlinePage(CoursePageReqVO dictDataPageReqVO) {
        return courseMapper.selectPage(dictDataPageReqVO);
    }

    @Override
    public Long createCourse(CourseCreateReqVO reqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        CourseDO courseOnlineDO = CourseChapterConvert.INSTANCE.convert(reqVO);
        courseMapper.insert(courseOnlineDO);
        return courseOnlineDO.getId();
    }

    @Override
    public CourseDO getOnlineInfo(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public void updateOnlineInfo(CourseUpdateReqVO updateReqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        // 校验唯一性
        validateCourseOnlineExists(updateReqVO.getId());
        // 更新
        CourseDO convert = CourseChapterConvert.INSTANCE.convert(updateReqVO);
        courseMapper.updateById(convert);
    }

    @Override
    public void deleteOnlineInfo(Long courseId) {
        // 校验课程存在
        validateCourseExists(courseId);
        // 删除课程
        courseMapper.deleteById(courseId);
    }

    @Override
    public void deleteBatchOnlineInfo(Collection<Long> ids) {
        List<CourseDO> collect = ids.stream().map(id -> {
            validateCourseExists(id);
            CourseDO courseOnlineDO = new CourseDO();
            return courseOnlineDO.setId(id);
        }).collect(Collectors.toList());
        courseMapper.deleteBatchIds(collect);
    }

    @Override
    public void updateStatusOnlineInfo(CourseUpdateStatusReqVO updateReqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        // TODO 这里希望来一个专属的status更新的逻辑
        // TODO j-sentinel 这里的状态需要进一步来确定有没有节的信息
        // 校验唯一性
        validateCourseOnlineExists(updateReqVO.getId());
        // 更新
        CourseDO convert = CourseChapterConvert.INSTANCE.convert(updateReqVO);
        courseMapper.updateById(convert);
    }

    @Override
    public List<CourseDO> getCourseOnlineList() {
        return courseMapper.findCourseList();
    }

    private void validateCourseExists(Long courseId) {
        if(courseId == null){
            return;
        }
        CourseDO courseOnlineDO = courseMapper.selectById(courseId);
        if(courseOnlineDO == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COURSE_CHAPTER_NOT_EXISTS);
        }
    }

    private void validateCourseOnlineExists(Long id) {
        if (courseMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COURSE_CHAPTER_NOT_EXISTS);
        }
    }

}
