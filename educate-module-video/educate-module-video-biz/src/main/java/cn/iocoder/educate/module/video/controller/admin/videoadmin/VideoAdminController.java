package cn.iocoder.educate.module.video.controller.admin.videoadmin;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminRespVo;
import cn.iocoder.educate.module.video.covert.videoadmin.VideoAdminConvert;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import cn.iocoder.educate.module.video.service.videoadmin.VideoAdminService;
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
    @PreAuthorize("@lanxin.hasPermission('video:file:query')")
    public CommonResult<PageResult<VideoAdminRespVo>> getVideoPage(@Valid VideoAdminPageReqVO videoPageReqVO) {
        PageResult<VideoAdminDO> pageResult = videoAdminService.getVideoAdminPage(videoPageReqVO);
        return success(VideoAdminConvert.INSTANCE.convertPage(pageResult));
    }

}
