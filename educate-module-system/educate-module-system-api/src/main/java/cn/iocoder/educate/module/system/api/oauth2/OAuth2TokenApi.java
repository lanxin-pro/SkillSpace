package cn.iocoder.educate.module.system.api.oauth2;

import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;

/**
 * OAuth2.0 Token API 接口
 *
 * @Author: 董伟豪
 * @Date: 2023/5/17 15:08
 */
public interface OAuth2TokenApi {

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken);
}
