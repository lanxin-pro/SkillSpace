package cn.iocoder.educate.module.video.controller.videoadmin;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/26 16:20
 */
@Tag(name = "管理后台 - 视频接口")
@RestController
@RequestMapping("/video/admin")
@Validated
public class VideoAdminController {

    @GetMapping("/load")
    @Operation(summary = "获得用户分页列表")
    public CommonResult getUserPage() {
        return null;
    }

}
