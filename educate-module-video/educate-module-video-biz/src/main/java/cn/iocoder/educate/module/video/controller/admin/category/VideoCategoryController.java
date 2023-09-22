package cn.iocoder.educate.module.video.controller.admin.category;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.VideoCategoryRespVO;
import cn.iocoder.educate.module.video.service.category.VideoCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:13
 */
@Tag(name = "管理后台 - 视频分类")
@RestController
@RequestMapping("/video/category")
@Validated
public class VideoCategoryController {

    @Resource
    private VideoCategoryService videoCategoryService;

    @PostMapping("/tree")
    @Operation(summary = "父子结构的分类信息")
    @PreAuthorize("@lanxin.hasPermission('video:category:tree')")
    public CommonResult<List<VideoCategoryRespVO>> createNotice() {
        List<VideoCategoryRespVO> videoCategoriesTree = videoCategoryService.findVideoCategoriesTree();
        return success(videoCategoriesTree);
    }

}
