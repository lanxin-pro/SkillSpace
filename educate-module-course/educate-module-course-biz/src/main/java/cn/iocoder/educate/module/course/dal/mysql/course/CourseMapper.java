package cn.iocoder.educate.module.course.dal.mysql.course;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.course.controller.admin.course.vo.CoursePageReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.course.CourseDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/28 11:46
 */
@Mapper
public interface CourseMapper extends BaseMapper<CourseDO> {

    default PageResult<CourseDO> selectPage(CoursePageReqVO courseOnlinePageReqVO){
        LambdaQueryWrapper<CourseDO> courseOnlineDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 过滤掉不需要字段
        // TODO j-sentinel 我觉得这个直接写不大优雅，字符串里面的内容后续可以进行优化
        courseOnlineDOLambdaQueryWrapper.select(CourseDO.class,
                column -> !column.getColumn().equals("content") &&
                        !column.getColumn().equals("html_content"));
        // 多列搜索 根据标题和标签和分类名进行模糊搜索
        courseOnlineDOLambdaQueryWrapper.and(StringUtils.hasText(courseOnlinePageReqVO.getKeyword()),
                wrapper -> wrapper.like(CourseDO::getTitle, courseOnlinePageReqVO.getKeyword())
                        .or()
                        .like(CourseDO::getCategoryTitle, courseOnlinePageReqVO.getKeyword())
                        .or()
                        .like(CourseDO::getNickname, courseOnlinePageReqVO.getKeyword())
                        .or()
                        .like(CourseDO::getTags, courseOnlinePageReqVO.getKeyword())
        );
        // 如果直接搜索 keyword 的话，预计就会出现问题，但是并没有真正测过，真实性有待后续（这里只是测试如果不加括号的是错误语法）
        // where title like '%k%' or tags like '%k%' and course_type = 1 and status = 1 and category_id = 1
        // where title like '%k%' or tags like '%k%' and course_type = 1 and status = 1 and category_id = 1

        // 但是如果加上and的话，SQL就会改变成
        // where (title like '%k%' or tags like '%k%') course_type = 1 and status = 1 and category_id = 1

        // 根据分类查询
        courseOnlineDOLambdaQueryWrapper
                .eq(ObjectUtil.isNotEmpty(courseOnlinePageReqVO.getCategoryId()),
                        CourseDO::getCategoryId, courseOnlinePageReqVO.getCategoryId())
                // 根据类型查询
                .eq(ObjectUtil.isNotEmpty(courseOnlinePageReqVO.getCourseType()),
                        CourseDO::getCourseType, courseOnlinePageReqVO.getCourseType())
                // 查询发布的 0 未发布  1 发布
                .eq(ObjectUtil.isNotEmpty(courseOnlinePageReqVO.getStatus()),
                        CourseDO::getStatus, courseOnlinePageReqVO.getStatus())
                .between(null != ArrayUtils.get(courseOnlinePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(courseOnlinePageReqVO.getCreateTime(),1) != null,
                        CourseDO::getCreateTime,
                        ArrayUtils.get(courseOnlinePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(courseOnlinePageReqVO.getCreateTime(),1))
                .orderByDesc(CourseDO::getCreateTime);
        Page<CourseDO> page = new Page<>(courseOnlinePageReqVO.getPageNo(), courseOnlinePageReqVO.getPageSize());
        Page<CourseDO> courseOnlineDOPage = this.selectPage(page, courseOnlineDOLambdaQueryWrapper);
        return new PageResult<>(courseOnlineDOPage.getRecords(), courseOnlineDOPage.getTotal());
    }

    default List<CourseDO> findCourseList(){
        LambdaQueryWrapper<CourseDO> courseOnlineDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 过滤掉不需要字段
        courseOnlineDOLambdaQueryWrapper.select(CourseDO.class, colum ->
                !colum.getColumn().equals("content") && !colum.getColumn().equals("htmlContent"));
        return this.selectList(courseOnlineDOLambdaQueryWrapper);
    }
}
