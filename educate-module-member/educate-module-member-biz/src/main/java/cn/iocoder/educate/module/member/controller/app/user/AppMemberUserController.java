package cn.iocoder.educate.module.member.controller.app.user;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.educate.module.member.convert.user.MemberUserConvert;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.service.level.MemberLevelService;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/31 12:34
 */
@Tag(name = "用户 APP - 用户个人中心")
@RestController
@RequestMapping("/member/user")
@Validated
@Slf4j
public class AppMemberUserController {

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private MemberLevelService memberLevelService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    @PreAuthenticated
    public CommonResult<AppMemberUserInfoRespVO> getUserInfo() {
        MemberUserDO user = memberUserService.getUser(SecurityFrameworkUtils.getLoginUserId());
        MemberLevelDO level = memberLevelService.getLevel(user.getLevelId());
        return CommonResult.success(MemberUserConvert.INSTANCE.convert(user, level));
    }

}
