package cn.iocoder.educate.module.course.controller.admin.online;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.*;
import cn.iocoder.educate.module.course.convert.online.CourseOnlineConvert;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;
import cn.iocoder.educate.module.course.service.online.CourseOnlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Collection;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;


/**
 * @author j-sentinel
 * @date 2024/1/28 11:25
 */
@Tag(name = "管理后台 - 课程")
@RestController
@RequestMapping("/course/online")
@Validated
public class CourseOnlineController {

    @Resource
    private CourseOnlineService onlineInfoService;

    @GetMapping("/page")
    @Operation(summary = "/获得课程的分页列表")
    @PreAuthorize("@lanxin.hasPermission('course:online:query')")
    public CommonResult<PageResult<CourseOnlineRespVO>> getDictTypePage(@Valid CourseOnlinePageReqVO dictDataPageReqVO) {
        PageResult<CourseOnlineDO> courseOnlineDOPageResult = onlineInfoService.getCourseOnlinePage(dictDataPageReqVO);
        return success(CourseOnlineConvert.INSTANCE.convertPage(courseOnlineDOPageResult));
    }

    @PostMapping("/create")
    @Operation(summary = "新增课程的章")
    @PreAuthorize("@lanxin.hasPermission('course:online:create')")
    public CommonResult<Long> createCourse(@Valid @RequestBody CourseOnlineCreateReqVO reqVO) {
        Long id = onlineInfoService.createCourse(reqVO);
        return success(id);
    }

    @GetMapping("/get")
    @Operation(summary = "获得课程信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('course:online:query')")
    public CommonResult<CourseOnlineRespVO> getOnlineInfo(@RequestParam("id") Long id) {
        CourseOnlineDO onlineInfo = onlineInfoService.getOnlineInfo(id);
        return success(CourseOnlineConvert.INSTANCE.convert(onlineInfo));
    }

    @PutMapping("/update")
    @Operation(summary = "更新课程信息")
    @PreAuthorize("@lanxin.hasPermission('course:online:update')")
    public CommonResult<Boolean> updateOnlineInfo(@Valid @RequestBody CourseOnlineUpdateReqVO updateReqVO) {
        onlineInfoService.updateOnlineInfo(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateStatus")
    @Operation(summary = "更新课程信息")
    @PreAuthorize("@lanxin.hasPermission('course:online:updateStatus')")
    public CommonResult<Boolean> updateStatusOnlineInfo(@Valid @RequestBody CourseOnlineUpdateStatusReqVO updateReqVO) {
        onlineInfoService.updateStatusOnlineInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除课程信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('course:online:delete')")
    public CommonResult<Boolean> deleteOnlineInfo(@RequestParam("id") Long id) {
        onlineInfoService.deleteOnlineInfo(id);
        return success(true);
    }

    @DeleteMapping("/delBatch")
    @Operation(summary = "根据课程ids批量删除课程")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@lanxin.hasPermission('course:online:delBatch')")
    public CommonResult<Boolean> deleteOnlineInfo(@RequestParam("ids") Collection<Long> ids) {
        onlineInfoService.deleteBatchOnlineInfo(ids);
        return success(true);
    }

}
