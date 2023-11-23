package cn.iocoder.educate.module.member.controller.admin.level;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.*;
import cn.iocoder.educate.module.member.convert.level.MemberLevelConvert;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.service.level.MemberLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/22 21:25
 */
@Tag(name = "管理后台 - 会员等级")
@RestController
@RequestMapping("/member/level")
@Validated
public class MemberLevelController {

    @Resource
    private MemberLevelService memberLevelService;

    @GetMapping("/page")
    @Operation(summary = "获得会员等级列表")
    @PreAuthorize("@lanxin.hasPermission('member:level:query')")
    public CommonResult<PageResult<MemberLevelRespVO>> getLevelPage(@Valid MemberLevelPageReqVO pageReqVO) {
        PageResult<MemberLevelDO> result = memberLevelService.getLevelPage(pageReqVO);
        return success(MemberLevelConvert.INSTANCE.convertPage(result));
    }

    @PostMapping("/create")
    @Operation(summary = "创建会员等级")
    @PreAuthorize("@lanxin.hasPermission('member:level:create')")
    public CommonResult<Long> createLevel(@Valid @RequestBody MemberLevelCreateReqVO createReqVO) {
        return success(memberLevelService.createLevel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员等级")
    @PreAuthorize("@lanxin.hasPermission('member:level:update')")
    public CommonResult<Boolean> updateLevel(@Valid @RequestBody MemberLevelUpdateReqVO updateReqVO) {
        memberLevelService.updateLevel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员等级")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('member:level:delete')")
    public CommonResult<Boolean> deleteLevel(@RequestParam("id") Long id) {
        memberLevelService.deleteLevel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员等级")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('member:level:query')")
    public CommonResult<MemberLevelRespVO> getLevel(@RequestParam("id") Long id) {
        MemberLevelDO level = memberLevelService.getLevel(id);
        return success(MemberLevelConvert.INSTANCE.convert(level));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取会员等级精简信息列表", description = "只包含被开启的会员等级，主要用于前端的下拉选项")
    public CommonResult<List<MemberLevelSimpleRespVO>> getSimpleLevelList() {
        // 获用户列表，只要开启状态的
        List<MemberLevelDO> list = memberLevelService.getEnableLevelList();
        // 排序后，返回给前端
        return success(MemberLevelConvert.INSTANCE.convertSimpleList(list));
    }

}
