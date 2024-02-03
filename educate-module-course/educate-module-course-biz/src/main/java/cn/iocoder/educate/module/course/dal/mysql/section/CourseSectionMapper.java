package cn.iocoder.educate.module.course.dal.mysql.section;

import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 20:05
 */
@Mapper
public interface CourseSectionMapper extends BaseMapper<CourseSectionDO> {

    default List<CourseSectionDO> findCourseChapterList(String courseId){
        LambdaQueryWrapper<CourseSectionDO> courseSectionDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseSectionDOLambdaQueryWrapper.eq(CourseSectionDO::getCourseId, courseId);
        courseSectionDOLambdaQueryWrapper.eq(CourseSectionDO::getPid, 0);
        // 删除以后，再次调用接口，把删除的放到下面去（前端）
        // TODO j-sentinel 这里其实可以细分权重
        courseSectionDOLambdaQueryWrapper.orderByDesc(CourseSectionDO::getDeleted, CourseSectionDO::getSorted);
        return this.selectList(courseSectionDOLambdaQueryWrapper);
    }

    default List<CourseSectionDO> findCourseSectionList(Long id) {
        LambdaQueryWrapper<CourseSectionDO> courseSectionDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseSectionDOLambdaQueryWrapper.eq(CourseSectionDO::getPid, id)
                .orderByDesc(CourseSectionDO::getSorted, CourseSectionDO::getId);
        return this.selectList(courseSectionDOLambdaQueryWrapper);
    }

}
