package cn.iocoder.educate.module.course.controller.admin.online;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.object.BeanUtils;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlinePageReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineRespVO;
import cn.iocoder.educate.module.course.convert.online.CourseOnlineConvert;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;
import cn.iocoder.educate.module.course.service.online.CourseOnlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;

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
    @PreAuthorize("@lanxin.hasPermission('system:course:query')")
    public CommonResult<PageResult<CourseOnlineRespVO>> getDictTypePage(@Valid CourseOnlinePageReqVO dictDataPageReqVO) {
        PageResult<CourseOnlineDO> courseOnlineDOPageResult = onlineInfoService.getCourseOnlinePage(dictDataPageReqVO);
        return success(CourseOnlineConvert.INSTANCE.convertPage(courseOnlineDOPageResult));
    }

}
