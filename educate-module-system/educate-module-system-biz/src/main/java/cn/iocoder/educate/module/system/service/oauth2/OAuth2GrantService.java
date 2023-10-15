package cn.iocoder.educate.module.system.service.oauth2;

import java.util.List;

/**
 * OAuth2 授予 Service 接口
 *
 * 从功能上，和 Spring Security OAuth 的 TokenGranter 的功能，提供访问令牌、刷新令牌的操作
 *
 * @Author: j-sentinel
 * @Date: 2023/10/15 19:52
 */
public interface OAuth2GrantService {

    /**
     * 授权码模式，第一阶段，获得 code 授权码
     *
     * 对应 Spring Security OAuth2 的 AuthorizationEndpoint 的 generateCode 方法
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param scopes 授权范围
     * @param redirectUri 重定向 URI
     * @param state 状态
     * @return 授权码
     */
    String grantAuthorizationCodeForCode(Long userId, Integer userType,
                                         String clientId, List<String> scopes,
                                         String redirectUri, String state);

}
