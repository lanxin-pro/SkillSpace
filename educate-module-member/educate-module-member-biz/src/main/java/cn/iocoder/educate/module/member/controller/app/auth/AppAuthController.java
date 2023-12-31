package cn.iocoder.educate.module.member.controller.app.auth;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsLoginReqVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsSendReqVO;
import cn.iocoder.educate.module.member.service.auth.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/4 19:30
 */
@Tag(name = "用户 APP - 认证")
@RestController
@RequestMapping("/member/auth")
@Validated
@Slf4j
public class AppAuthController {

    @Resource
    private MemberAuthService memberAuthService;

    // ========== 短信登录相关 ==========

    @PostMapping("/send-sms-code")
    @Operation(summary = "发送手机验证码")
    @PermitAll
    public CommonResult<Boolean> sendSmsCode(@RequestBody @Valid AppAuthSmsSendReqVO appAuthSmsSendReqVO) {
        memberAuthService.sendSmsCode(SecurityFrameworkUtils.getLoginUserId(), appAuthSmsSendReqVO);
        return CommonResult.success(true);
    }

    /**
     *
     * @param appAuthSmsLoginReqVO
     * @param terminal 必填字段
     * @return
     */
    @PostMapping("/sms-login")
    @Operation(summary = "使用手机 + 验证码登录")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> smsLogin(@RequestBody @Valid AppAuthSmsLoginReqVO appAuthSmsLoginReqVO,
                                                     @RequestHeader Integer terminal) {
        return CommonResult.success(memberAuthService.smsLogin(appAuthSmsLoginReqVO, terminal));
    }


}
