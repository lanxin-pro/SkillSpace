package cn.iocoder.educate.module.member.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.user.vo.MemberUserPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.user.vo.MemberUserRespVO;
import cn.iocoder.educate.module.member.convert.user.MemberUserConvert;
import cn.iocoder.educate.module.member.dal.dataobject.group.MemberGroupDO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.service.group.MemberGroupService;
import cn.iocoder.educate.module.member.service.level.MemberLevelService;
import cn.iocoder.educate.module.member.service.tag.MemberTagService;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/4/13 19:00
 */
@Tag(name = "管理后台 - 会员用户")
@RestController
@RequestMapping("/member/user")
@Validated
public class MemberUserController {

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private MemberTagService memberTagService;

    @Resource
    private MemberGroupService memberGroupService;

    @Resource
    private MemberLevelService memberLevelService;

    @GetMapping("/page")
    @Operation(summary = "获得会员用户分页")
    @PreAuthorize("@lanxin.hasPermission('member:user:query')")
    public CommonResult<PageResult<MemberUserRespVO>> getUserPage(@Valid MemberUserPageReqVO pageVO) {
        PageResult<MemberUserDO> pageResult = memberUserService.getUserPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return CommonResult.success(PageResult.empty());
        }

        // 处理用户标签返显
        Set<Long> tagIds = pageResult.getList().stream()
                .map(MemberUserDO::getTagIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        List<MemberTagDO> tags = memberTagService.getTagList(tagIds);
        // 处理用户级别返显
        List<MemberLevelDO> levels = memberLevelService.getLevelList(
                pageResult.getList()
                        .stream().map(MemberUserDO::getLevelId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()
                ));
        // 处理用户分组返显
        List<MemberGroupDO> groups = memberGroupService.getGroupList(
                pageResult.getList().stream()
                        .map(MemberUserDO::getGroupId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()));
        return CommonResult.success(MemberUserConvert.INSTANCE.convertPage(pageResult, tags, levels, groups));
    }
}
