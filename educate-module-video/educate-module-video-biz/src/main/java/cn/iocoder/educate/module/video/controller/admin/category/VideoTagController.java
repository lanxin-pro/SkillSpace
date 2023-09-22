package cn.iocoder.educate.module.video.controller.admin.category;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagReqVo;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagRespVO;
import cn.iocoder.educate.module.video.covert.category.VideoTagConvert;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoTagDO;
import cn.iocoder.educate.module.video.service.category.VideoTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:49
 */
@Tag(name = "管理后台 - 视频标签")
@RestController
@RequestMapping("/video/tag")
@Validated
public class VideoTagController {

    @Resource
    private VideoTagService videoTagService;

    @GetMapping("/page")
    @Operation(summary = "查询标签分页列表")
    @PreAuthorize("@lanxin.hasPermission('video:tag:page')")
    public CommonResult<PageResult<VideoTagRespVO>> findVideoPanelsPage(@Validated VideoTagReqVo videoTagListReq) {
        PageResult<VideoTagDO> videoPanelsPage = videoTagService.findVideoPanelsPage(videoTagListReq);
        return CommonResult.success(VideoTagConvert.INSTANCE.convertPage(videoPanelsPage));
    }


}
