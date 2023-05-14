package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:27
 */
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService{

    @Resource
    private OAuth2ClientService oauth2ClientService;

    @Override
    @Transactional
    public OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
        // 获得OAuth2的DO对象，OAuth2中客户端的定义
        OAuth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        return null;
    }
}
