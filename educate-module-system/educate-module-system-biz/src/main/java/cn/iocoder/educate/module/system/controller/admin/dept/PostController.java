package cn.iocoder.educate.module.system.controller.admin.dept;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostSimpleRespVO;
import cn.iocoder.educate.module.system.convert.post.PostConvert;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import cn.iocoder.educate.module.system.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
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
