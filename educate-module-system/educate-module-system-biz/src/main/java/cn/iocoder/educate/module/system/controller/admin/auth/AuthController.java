package cn.iocoder.educate.module.system.controller.admin.auth;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.util.collection.SetUtils;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.framework.security.config.SecurityProperties;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.*;
import cn.iocoder.educate.module.system.convert.auth.AuthConvert;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.educate.module.system.enums.permission.MenuTypeEnum;
import cn.iocoder.educate.module.system.service.auth.AdminAuthService;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import cn.iocoder.educate.module.system.service.permission.RoleService;
import cn.iocoder.educate.module.system.service.social.SocialUserService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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
    private AdminUserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private SocialUserService socialUserService;

    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/login")
    @Operation(summary = "使用账号密码登录")
    @PermitAll
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO authLoginReqVO){
        return success(authService.login(authLoginReqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    @OperateLog(enable = true)
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 获得用户信息
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserDO user = userService.getUser(loginUserId);
        if(user == null){
            return null;
        }
        // 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdsFromCache(loginUserId,
                CommonStatusEnum.ENABLE.getStatus());
        List<RoleDO> roleList = roleService.getRoleListFromCache(roleIds);
        List<MenuDO> menuList = permissionService.getRoleMenuListFromCache(roleIds,
                SetUtils.asSet(MenuTypeEnum.DIR.getType(),MenuTypeEnum.MENU.getType(),MenuTypeEnum.BUTTON.getType()),
                CommonStatusEnum.ENABLE.getStatus());
        // 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user,roleList,menuList));
    }

    @GetMapping("/list-menus")
    @Operation(summary = "获得登录用户的菜单列表")
    @OperateLog(enable = true)
    CommonResult<List<AuthMenuRespVO>> getMenuList() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 获得角色列表
        Set<Long> roleIdsFrom = permissionService.getUserRoleIdsFromCache(loginUserId, CommonStatusEnum.ENABLE.getStatus());
        // 获得用户拥有的菜单列表
        List<MenuDO> menuList = permissionService.getRoleMenuListFromCache(roleIdsFrom,
                SetUtils.asSet(MenuTypeEnum.DIR.getType(), MenuTypeEnum.MENU.getType()),
                CommonStatusEnum.ENABLE.getStatus());
        // 拼接结果返回
        return success(AuthConvert.INSTANCE.buildMenuTree(menuList));
    }


    // ========== 短信登录相关 ==========

    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送手机验证码")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> sendLoginSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        authService.sendSmsCode(reqVO);
        return success(true);
    }

    @PostMapping("/sms-login")
    @PermitAll
    @Operation(summary = "使用短信验证码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> smsLogin(@RequestBody @Valid AuthSmsLoginReqVO reqVO) {
        AuthLoginRespVO authLoginRespVO = authService.smsLogin(reqVO);
        return success(authLoginRespVO);
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
