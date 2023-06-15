package cn.iocoder.educate.module.system.controller.admin.auth;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSocialLoginReqVO;
import cn.iocoder.educate.module.system.service.auth.AdminAuthService;
import cn.iocoder.educate.module.system.service.social.SocialUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 18:04
 */
@Validated
@RestController
@RequestMapping("/system/auth")
@Tag(name = "管理后台 - 认证")
public class AuthController {

    @Resource
    private AdminAuthService authService;

    @Resource
    private SocialUserService socialUserService;

    @PostMapping("/login")
    @Operation(summary = "使用账号密码登录")
    @PermitAll
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO authLoginReqVO){
        return success(authService.login(authLoginReqVO));
    }

    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送手机验证码")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> sendLoginSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        authService.sendSmsCode(reqVO);
        return success(true);
    }



    // ========== 社交登录相关 ==========

    @GetMapping("/social-auth-redirect")
    @PermitAll
    @Operation(summary = "社交授权的跳转")
    @Parameters({
            @Parameter(name = "type", description = "社交类型", required = true),
            @Parameter(name = "redirectUri", description = "回调路径")
    })
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> socialLogin(@RequestParam("type") Integer type,
                                            @RequestParam("redirectUri") String redirectUri) {
        String socialUserServiceAuthorizeUrl = socialUserService.getAuthorizeUrl(type, redirectUri);
        return CommonResult.success(socialUserServiceAuthorizeUrl);
    }

    @PostMapping("/social-login")
    @PermitAll
    @Operation(summary = "社交快捷登录，使用 code 授权码", description = "适合未登录的用户，但是社交账号已绑定用户")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> socialQuickLogin(@RequestBody @Valid AuthSocialLoginReqVO reqVO) {
        AuthLoginRespVO authLoginRespVO = authService.socialLogin(reqVO);
        return CommonResult.success(authLoginRespVO);
    }
}
