package cn.iocoder.educate.module.system.controller.admin.dept;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.*;
import cn.iocoder.educate.module.system.convert.post.PostConvert;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import cn.iocoder.educate.module.system.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:32
 */
@Tag(name = "管理后台 - 岗位")
@RestController
@RequestMapping("/system/post")
@Validated
public class PostController {

    @Resource
    private PostService postService;

    @GetMapping("/page")
    @Operation(summary = "获得岗位分页列表")
    public CommonResult<PageResult<PostRespVO>> getPostPage(@Validated PostPageReqVO reqVO) {
        PageResult<PostDO> pageResult = postService.getPostPage(reqVO);
        return success(PostConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/create")
    @Operation(summary = "创建岗位")
    public CommonResult<Long> createPost(@Valid @RequestBody PostCreateReqVO reqVO) {
        Long postId = postService.createPost(reqVO);
        return success(postId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改岗位")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody PostUpdateReqVO reqVO) {
        postService.updatePost(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除岗位")
    public CommonResult<Boolean> deletePost(@RequestParam("id") Long id) {
        postService.deletePost(id);
        return success(true);
    }

    @GetMapping(value = "/get")
    @Operation(summary = "获得岗位信息")
    @Parameter(name = "id", description = "岗位编号", required = true, example = "1024")
    public CommonResult<PostRespVO> getPost(@RequestParam("id") Long id) {
        PostDO postDO = postService.getPost(id);
        return success(PostConvert.INSTANCE.convert(postDO));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取岗位精简信息列表", description = "只包含被开启的岗位，主要用于前端的下拉选项")
    public CommonResult<List<PostSimpleRespVO>> getSimplePostList() {
        // 获得岗位列表，只要开启状态的
        List<PostDO> list = postService.getPostList(null, Collections.singleton(CommonStatusEnum.ENABLE.getStatus()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PostDO::getSort));
        return success(PostConvert.INSTANCE.convertList02(list));
    }
}
