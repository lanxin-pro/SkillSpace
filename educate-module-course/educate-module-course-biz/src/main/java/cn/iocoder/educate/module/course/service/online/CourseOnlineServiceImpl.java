package cn.iocoder.educate.module.course.service.online;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlinePageReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineUpdateStatusReqVO;
import cn.iocoder.educate.module.course.convert.online.CourseOnlineConvert;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;
import cn.iocoder.educate.module.course.dal.mysql.online.CourseOnlineMapper;
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
public class CourseOnlineServiceImpl implements CourseOnlineService {

    @Resource
    private CourseOnlineMapper courseMapper;

    @Override
    public PageResult<CourseOnlineDO> getCourseOnlinePage(CourseOnlinePageReqVO dictDataPageReqVO) {
        return courseMapper.selectPage(dictDataPageReqVO);
    }

    @Override
    public Long createCourse(CourseOnlineCreateReqVO reqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        CourseOnlineDO courseOnlineDO = CourseOnlineConvert.INSTANCE.convert(reqVO);
        courseMapper.insert(courseOnlineDO);
        return courseOnlineDO.getId();
    }

    @Override
    public CourseOnlineDO getOnlineInfo(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public void updateOnlineInfo(CourseOnlineUpdateReqVO updateReqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        // 校验唯一性
        validateCourseOnlineExists(updateReqVO.getId());
        // 更新
        CourseOnlineDO convert = CourseOnlineConvert.INSTANCE.convert(updateReqVO);
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
        List<CourseOnlineDO> collect = ids.stream().map(id -> {
            validateCourseExists(id);
            CourseOnlineDO courseOnlineDO = new CourseOnlineDO();
            return courseOnlineDO.setId(id);
        }).collect(Collectors.toList());
        courseMapper.deleteBatchIds(collect);
    }

    @Override
    public void updateStatusOnlineInfo(CourseOnlineUpdateStatusReqVO updateReqVO) {
        // TODO 严重 j-sentinel 这里需要进行校验
        // TODO 这里希望来一个专属的status更新的逻辑
        // TODO j-sentinel 这里的状态需要进一步来确定有没有节的信息
        // 校验唯一性
        validateCourseOnlineExists(updateReqVO.getId());
        // 更新
        CourseOnlineDO convert = CourseOnlineConvert.INSTANCE.convert(updateReqVO);
        courseMapper.updateById(convert);
    }

    private void validateCourseExists(Long courseId) {
        if(courseId == null){
            return;
        }
        CourseOnlineDO courseOnlineDO = courseMapper.selectById(courseId);
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
