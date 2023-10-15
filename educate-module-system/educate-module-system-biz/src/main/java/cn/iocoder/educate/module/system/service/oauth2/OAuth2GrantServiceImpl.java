package cn.iocoder.educate.module.system.service.oauth2;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * OAuth2 授予 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/15 19:53
 */
@Service
public class OAuth2GrantServiceImpl implements OAuth2GrantService {

    @Resource
    private OAuth2CodeService oauth2CodeService;

    @Override
    public String grantAuthorizationCodeForCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state) {
        return oauth2CodeService.createAuthorizationCode(userId, userType, clientId, scopes,
                redirectUri, state).getCode();
    }
}
