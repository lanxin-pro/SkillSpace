package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:39
 */
@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService{

    @Override
    public OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType, Collection<String> scopes, String redirectUri) {
        return null;
    }
}
