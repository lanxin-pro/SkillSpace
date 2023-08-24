package cn.iocoder.educate.module.system.controller.admin.oauth2;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import cn.iocoder.educate.module.system.convert.oauth2.OAuth2OpenConvert;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2ApproveService;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 提供给外部应用调用为主
 * 一般来说，管理后台的 /system-api/* 是不直接提供给外部应用使用，主要是外部应用能够访问的数据与接口是有限的，而管理后台的 RBAC 无法很好的控制
 *
 * 参考大量的开放平台，都是独立的一套 OpenAPI，对应到【本系统】就是在 Controller 下新建 open 包，实现 /open-api/* 接口
 * 然后通过 scope 进行控制
 *
 * 一个公司如果有多个管理后台，它们 client_id 产生的 access token 相互之间是无法互通的，即无法访问它们系统的 API 接口
 * 直到两个 client_id 产生信任授权
 *
 * 考虑到【本系统】暂时不想做的过于复杂，默认只有获取到 access token 之后，可以访问【本系统】管理后台的 /system-api/* 所有接口
 * 除非手动添加 scope 控制
 *
 * @Author: j-sentinel
 * @Date: 2023/6/9 9:55
 */
@Tag(name = "管理后台 - OAuth2.0 授权")
@RestController
@RequestMapping("/system/oauth2")
@Validated
@Slf4j
public class OAuth2OpenController {

    @Resource
    private OAuth2ClientService oauth2ClientService;

    @Resource
    private OAuth2ApproveService oauth2ApproveService;

    /**
     * 对应 Spring Security OAuth 的 AuthorizationEndpoint 类的 authorize 方法
     * @param clientId
     * @return
     */
    @GetMapping("/authorize")
    @Operation(summary = "获得授权信息",
            description = "适合 code 授权码模式，或者 implicit 简化模式；在 SSOAccreditLogin.vue 单点登录界面被【获取】调用")
    @Parameter(name = "clientId", required = true, description = "客户端编号", example = "tudou")
    public CommonResult<OAuth2OpenAuthorizeInfoRespVO> authorize(@RequestParam("clientId") String clientId) {
        // 0. 校验用户已经登录。通过 Spring Security 实现

        // 1. 获得 Client 客户端的信息
        OAuth2ClientDO client = oauth2ClientService.validOAuthClientFromCache(clientId);
        // 2. 获得用户已经授权的信息
        List<OAuth2ApproveDO> approves = oauth2ApproveService.getApproveList(null, null, clientId);
        // 拼接返回
        OAuth2OpenAuthorizeInfoRespVO convert = OAuth2OpenConvert.INSTANCE.convert(client, approves);
        return CommonResult.success(convert);
    }

    /**
     * 对应 Spring Security OAuth 的 AuthorizationEndpoint 类的 approveOrDeny 方法
     *
     * 场景一：【自动授权 autoApprove = true】
     *      刚进入 sso.vue 界面，调用该接口，用户历史已经给该应用做过对应的授权，或者 OAuth2Client 支持该 scope 的自动授权
     * 场景二：【手动授权 autoApprove = false】
     *      在 sso.vue 界面，用户选择好 scope 授权范围，调用该接口，进行授权。此时，approved 为 true 或者 false
     *
     * 因为前后端分离，Axios 无法很好的处理 302 重定向，所以和 Spring Security OAuth 略有不同，返回结果是重定向的 URL，剩余交给前端处理
     */
    @PostMapping("/authorize")
    @Operation(summary = "申请授权", description = "适合 code 授权码模式，或者 implicit 简化模式；" +
            "在 sso.vue 单点登录界面被【提交】调用")
    @Parameters({
            @Parameter(name = "response_type", required = true, description = "响应类型", example = "code"),
            @Parameter(name = "client_id", required = true, description = "客户端编号", example = "tudou"),
            // 使用 Map<String, Boolean> 格式，Spring MVC 暂时不支持这么接收参数
            @Parameter(name = "scope", description = "授权范围", example = "userinfo.read"),
            @Parameter(name = "redirect_uri", required = true, description = "重定向 URI",
                    example = "https://www.iocoder.cn"),
            @Parameter(name = "auto_approve", required = true, description = "用户是否接受", example = "true"),
            @Parameter(name = "state", example = "1")
    })
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> approveOrDeny(@RequestParam("response_type") String responseType,
                                              @RequestParam("client_id") String clientId,
                                              @RequestParam(value = "scope", required = false) String scope,
                                              @RequestParam("redirect_uri") String redirectUri,
                                              @RequestParam(value = "auto_approve") Boolean autoApprove,
                                              @RequestParam(value = "state", required = false) String state) {
            return null;
    }

}
