package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;

import java.util.Collection;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/12 21:34
 */
public interface OAuth2ClientService {

    /**
     * 从缓存中，校验客户端是否合法
     * @param clientId
     * @return
     */
    default OAuth2ClientDO validOAuthClientFromCache(String clientId){
        return validOAuthClientFromCache(clientId,null,null,null,null);
    }

    /**
     * 从缓存中，校验客户端是否合法
     *
     * 非空时，进行校验
     *
     * @param clientId 客户端编号
     * @param clientSecret 客户端密钥
     * @param authorizedGrantType 授权方式
     * @param scopes 授权范围
     * @param redirectUri 重定向地址
     * @return 客户端
     */
    OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret,
                                             String authorizedGrantType, Collection<String> scopes, String redirectUri);

}
