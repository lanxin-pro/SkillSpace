package cn.iocoder.educate.module.course.controller.admin.chapter;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterReqVO;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.convert.section.CourseSectionConvert;
import cn.iocoder.educate.module.course.dal.dataobject.chapter.CourseChapterDO;
import cn.iocoder.educate.module.course.service.chapter.CourseChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:27
 */
@Tag(name = "管理后台 - 课程-章节")
@RestController
@RequestMapping("/course/online/chapter")
@Validated
public class CourseChapterController {

    @Resource
    private CourseChapterService courseSectionService;

    @GetMapping("/list")
    @Operation(summary = "/查询章节管理列表信息")
    @PreAuthorize("@lanxin.hasPermission('online:chapter:query')")
    public CommonResult<List<CourseChapterRespVO>> getDictTypeList(@RequestParam("courseId") String courseId) {
        // TODO j-sentinel 这里的逻辑需要优化到service层
        List<CourseChapterRespVO> chapterList = courseSectionService.findCourseChapterList(courseId);
        if(CollectionUtil.isNotEmpty(chapterList)) {
            // 把下面的节也全部查询出来
            chapterList = chapterList.stream().map(chapter -> {
                List<CourseChapterRespVO> courseSectionList = courseSectionService.findCourseSectionList(chapter.getId());
                chapter.setSectionList(courseSectionList);
                return chapter;
            }).collect(Collectors.toList());
        }
        return success(chapterList);
    }

    @PostMapping("/saveupdate")
    @Operation(summary = "/查询章节管理列表信息")
    @PreAuthorize("@lanxin.hasPermission('online:chapter:save')")
    public CommonResult<CourseChapterRespVO> saveUpdateChapterLesson(@RequestBody CourseChapterReqVO courseSectionReqVO) {
        CourseChapterDO courseSectionDO = courseSectionService.saveUpdateChapterLesson(courseSectionReqVO);
        return success(CourseSectionConvert.INSTANCE.convert(courseSectionDO));
    }

    @GetMapping("/get")
    @Operation(summary = "/查询章节管理列表信息")
    @PreAuthorize("@lanxin.hasPermission('online:chapter:get')")
    public CommonResult<CourseChapterRespVO> getChapterLessons(@RequestParam("opid") Long opid) {
        // 本章信息
        CourseChapterDO chapterLessons = courseSectionService.getChapterLessons(opid);
        return success(CourseSectionConvert.INSTANCE.convert(chapterLessons));
    }

}
