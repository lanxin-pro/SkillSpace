package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Map;
import static cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.educate.module.system.enums.ErrorCodeConstants.*;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:39
 */
@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService{

    /**
     * 客户端缓存
     *
     * key：客户端编号 {@link OAuth2ClientDO#getClientId()} ()}
     *
     * 禁止线程的工作内存对volatile修饰的变量进行缓存
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Map<String, OAuth2ClientDO> clientCache;

    @Override
    public OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType, Collection<String> scopes, String redirectUri) {
        // 校验客户端存在、且开启
        OAuth2ClientDO oAuth2ClientDO = clientCache.get(clientId);
        if (ObjectUtil.isEmpty(oAuth2ClientDO)) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
        if(ObjectUtil.notEqual(oAuth2ClientDO.getStatus(), CommonStatusEnum.ENABLE.getStatus())){
            throw exception(OAUTH2_CLIENT_DISABLE);
        }

        return null;
    }
}
