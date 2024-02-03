package cn.iocoder.educate.module.course.controller.admin.section;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.course.controller.admin.chapter.vo.CourseChapterRespVO;
import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionRespVO;
import cn.iocoder.educate.module.course.convert.section.CourseSectionConvert;
import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;
import cn.iocoder.educate.module.course.service.section.CourseSectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:27
 */
@Tag(name = "管理后台 - 课程-节")
@RestController
@RequestMapping("/course/online/section")
@Validated
public class CourseSectionController {

    @Resource
    private CourseSectionService courseSectionService;

    @GetMapping("/list")
    @Operation(summary = "/查询章节管理列表信息")
    @PreAuthorize("@lanxin.hasPermission('course:section:query')")
    public CommonResult<List<CourseSectionRespVO>> getDictTypeList(@RequestParam("courseId") String courseId) {
        // TODO j-sentinel 这里的逻辑需要优化到service层
        List<CourseSectionRespVO> chapterList = courseSectionService.findCourseChapterList(courseId);
        if(CollectionUtil.isNotEmpty(chapterList)) {
            // 把下面的节也全部查询出来
            chapterList = chapterList.stream().map(chapter -> {
                List<CourseSectionRespVO> courseSectionList = courseSectionService.findCourseSectionList(chapter.getId());
                chapter.setSectionList(courseSectionList);
                return chapter;
            }).collect(Collectors.toList());
        }
        return success(chapterList);
    }


}
