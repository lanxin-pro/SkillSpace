package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import java.util.List;

/**
 * OAuth2.0 Token Service 接口
 *
 * 从功能上，和 Spring Security OAuth 的 DefaultTokenServices + JdbcTokenStore 的功能，提供访问令牌、刷新令牌的操作
 *
 * @Author: 董伟豪
 * @Date: 2023/5/12 21:26
 */
public interface OAuth2TokenService {

    /**
     * 创建访问令牌
     * 注意：该流程中，会包含创建刷新令牌的创建
     *
     * 参考 DefaultTokenServices 的 createAccessToken 方法
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param scopes 授权范围
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes);

    /**
     * 校验访问令牌
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO checkAccessToken(String accessToken);

    /**
     * 移除访问令牌
     * 注意：该流程中，会移除相关的刷新令牌
     *
     * 参考 DefaultTokenServices 的 revokeToken 方法
     *
     * @param accessToken 刷新令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO removeAccessToken(String accessToken);
}
