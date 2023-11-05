package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.educate.framework.common.exception.ErrorCode;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2CodeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OAuth2.0 授权码 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/15 19:57
 */
@Service
@Validated
public class OAuth2CodeServiceImpl implements OAuth2CodeService {

    /**
     * 授权码的过期时间，默认 5 分钟
     */
    private static final Integer TIMEOUT = 5 * 60;

    @Resource
    private OAuth2CodeMapper oauth2CodeMapper;

    @Override
    public OAuth2CodeDO createAuthorizationCode(Long userId, Integer userType, String clientId, List<String> scopes, String redirectUri, String state) {
        OAuth2CodeDO codeDO = new OAuth2CodeDO().setCode(generateCode())
                .setUserId(userId).setUserType(userType)
                .setClientId(clientId).setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(TIMEOUT))
                .setRedirectUri(redirectUri).setState(state);
        oauth2CodeMapper.insert(codeDO);
        return codeDO;
    }

    @Override
    public OAuth2CodeDO consumeAuthorizationCode(String code) {
        OAuth2CodeDO codeDO = oauth2CodeMapper.selectByCode(code);
        if (codeDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CODE_NOT_EXISTS);
        }
        // 过期时间的判断
        if (DateUtils.isExpired(codeDO.getExpiresTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OAUTH2_CODE_EXPIRE);
        }
        oauth2CodeMapper.deleteById(codeDO.getId());
        return codeDO;
    }

    private static String generateCode() {
        return IdUtil.fastSimpleUUID();
    }

}
