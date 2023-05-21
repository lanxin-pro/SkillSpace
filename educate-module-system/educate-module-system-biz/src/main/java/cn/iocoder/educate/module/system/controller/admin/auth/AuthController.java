package cn.iocoder.educate.module.system.controller.admin.auth;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.educate.module.system.service.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/login")
    @Operation(summary = "使用账号密码登录")
    @PermitAll
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO authLoginReqVO){
        return success(authService.login(authLoginReqVO));
    }

    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送手机验证码")
    public CommonResult<Boolean> sendLoginSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        authService.sendSmsCode(reqVO);
        return success(true);
    }

}
