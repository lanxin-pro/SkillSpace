package cn.iocoder.educate.module.system.api.oauth2;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.educate.module.system.convert.auth.OAuth2TokenConvert;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2TokenService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * OAuth2.0 Token API 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/17 15:27
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        return OAuth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken));
    }

    @Override
    public OAuth2AccessTokenRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(), reqDTO.getUserType(), reqDTO.getClientId(), reqDTO.getScopes());
        return BeanUtil.toBean(accessTokenDO, OAuth2AccessTokenRespDTO.class);
    }
}
