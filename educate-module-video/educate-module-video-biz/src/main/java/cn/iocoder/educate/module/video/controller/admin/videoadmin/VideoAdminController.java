package cn.iocoder.educate.module.video.controller.admin.videoadmin;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminCreateReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminRespVo;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminUpdateReqVO;
import cn.iocoder.educate.module.video.covert.videoadmin.VideoAdminConvert;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import cn.iocoder.educate.module.video.service.videoadmin.VideoAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/26 16:20
 */
@Tag(name = "管理后台 - 视频接口")
@RestController
@RequestMapping("/video/admin")
@Validated
public class VideoAdminController {

    @Resource
    private VideoAdminService videoAdminService;

    @GetMapping("/page")
    @Operation(summary = "获得视频详情分页")
    @PreAuthorize("@lanxin.hasPermission('video:admin:query')")
    public CommonResult<PageResult<VideoAdminRespVo>> getVideoPage(@Valid VideoAdminPageReqVO videoPageReqVO) {
        PageResult<VideoAdminDO> pageResult = videoAdminService.getVideoAdminPage(videoPageReqVO);
        return success(VideoAdminConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/create")
    @Operation(summary = "获得视频详情分页")
    @PreAuthorize("@lanxin.hasPermission('video:admin:create')")
    public CommonResult<Long> createVideo(@Valid @RequestBody VideoAdminCreateReqVO liveStudioSourceManegeVO) {
        Long adminId = videoAdminService.createVideo(liveStudioSourceManegeVO);
        return success(adminId);
    }

    @PutMapping("/update")
    @Operation(summary = "获得视频详情分页")
    @PreAuthorize("@lanxin.hasPermission('video:admin:update')")
    public CommonResult<Boolean> updateVideo(@Valid @RequestBody VideoAdminUpdateReqVO videoAdminUpdateReqVO) {
        videoAdminService.updateVideo(videoAdminUpdateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得视频信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('video:admin:get')")
    public CommonResult<VideoAdminRespVo> getMailAccount(@RequestParam("id") Long id) {
        VideoAdminDO videoAdminDO = videoAdminService.getFileVideo(id);
        return success(VideoAdminConvert.INSTANCE.convert(videoAdminDO));
    }

}
