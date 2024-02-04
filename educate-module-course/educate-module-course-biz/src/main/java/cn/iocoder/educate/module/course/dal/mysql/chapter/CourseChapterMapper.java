package cn.iocoder.educate.module.course.dal.mysql.chapter;

import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 20:05
 */
@Mapper
public interface CourseChapterMapper extends BaseMapper<CourseChapterDO> {

    default List<CourseChapterDO> findCourseChapterList(String courseId){
        LambdaQueryWrapper<CourseChapterDO> courseSectionDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseSectionDOLambdaQueryWrapper.eq(CourseChapterDO::getCourseId, courseId);
        courseSectionDOLambdaQueryWrapper.eq(CourseChapterDO::getPid, 0);
        // 删除以后，再次调用接口，把删除的放到下面去（前端）
        // TODO j-sentinel 这里其实可以细分权重
        courseSectionDOLambdaQueryWrapper.orderByDesc(CourseChapterDO::getIsDelete, CourseChapterDO::getSorted);
        return this.selectList(courseSectionDOLambdaQueryWrapper);
    }

    default List<CourseChapterDO> findCourseSectionList(Long id) {
        LambdaQueryWrapper<CourseChapterDO> courseSectionDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseSectionDOLambdaQueryWrapper.eq(CourseChapterDO::getPid, id)
                .orderByDesc(CourseChapterDO::getIsDelete, CourseChapterDO::getSorted);
        return this.selectList(courseSectionDOLambdaQueryWrapper);
    }

}
