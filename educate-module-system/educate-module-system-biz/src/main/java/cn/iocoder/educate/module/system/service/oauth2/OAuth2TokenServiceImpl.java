package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import cn.iocoder.educate.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2AccessTokenMapper;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2RefreshTokenMapper;
import cn.iocoder.educate.module.system.dal.redis.oauth2.OAuth2AccessTokenRedisDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import static cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil.exception0;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:27
 */
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService{

    @Resource
    private OAuth2ClientService oauth2ClientService;

    @Resource
    private OAuth2RefreshTokenMapper oauth2RefreshTokenMapper;

    @Resource
    private OAuth2AccessTokenMapper oauth2AccessTokenMapper;

    @Resource
    private OAuth2AccessTokenRedisDAO oauth2AccessTokenRedisDAO;

    @Override
    @Transactional
    public OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
        // 获得OAuth2的DO对象，OAuth2中客户端的定义
        OAuth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        // 创建刷新令牌
        OAuth2RefreshTokenDO refreshTokenDO = createOAuth2RefreshToken(userId, userType, clientDO, scopes);
        // 创建访问令牌(这里增加访问速度的话会写入到redis当中去)
        return createOAuth2AccessToken(refreshTokenDO,clientDO);
    }

    @Override
    public OAuth2AccessTokenDO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = getAccessToken(accessToken);
        if (accessTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌不存在");
        }
        if (DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌已过期");
        }
        return accessTokenDO;
    }

    public OAuth2AccessTokenDO getAccessToken(String accessToken) {
        // 优先从 Redis 中获取
        OAuth2AccessTokenDO accessTokenDO = oauth2AccessTokenRedisDAO.get(accessToken);
        if (accessTokenDO != null) {
            return accessTokenDO;
        }

        // 获取不到，从 MySQL 中获取
        accessTokenDO = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        // 如果在 MySQL 存在，则往 Redis 中写入
        if (accessTokenDO != null && !DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
            oauth2AccessTokenRedisDAO.set(accessTokenDO);
        }
        return accessTokenDO;
    }

    private OAuth2AccessTokenDO createOAuth2AccessToken(OAuth2RefreshTokenDO refreshTokenDO, OAuth2ClientDO clientDO) {
        OAuth2AccessTokenDO accessTokenDO = new OAuth2AccessTokenDO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDO.getUserId()).setUserType(refreshTokenDO.getUserType())
                .setClientId(clientDO.getClientId()).setScopes(refreshTokenDO.getScopes())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                // plusSeconds 添加指定的秒数
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getAccessTokenValiditySeconds()));
        accessTokenDO.setTenantId(TenantContextHolder.getTenantId()); // 手动设置租户编号，避免缓存到 Redis 的时候，无对应的租户编号
        oauth2AccessTokenMapper.insert(accessTokenDO);
        // 记录到 Redis 中
        oauth2AccessTokenRedisDAO.set(accessTokenDO);
        return accessTokenDO;
    }

    /**
     * 创建刷新令牌
     *
     * @param userId 用户id
     * @param userType 用户类型
     * @param clientDO 客户端信息
     * @param scopes 作用域
     * @return
     */
    private OAuth2RefreshTokenDO createOAuth2RefreshToken(Long userId, Integer userType, OAuth2ClientDO clientDO,
                                                          List<String> scopes) {
        OAuth2RefreshTokenDO refreshToken = new OAuth2RefreshTokenDO().setRefreshToken(generateRefreshToken())
                .setUserId(userId).setUserType(userType)
                .setClientId(clientDO.getClientId()).setScopes(scopes)
                // plusSeconds 添加指定的秒数
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getRefreshTokenValiditySeconds()));
        oauth2RefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return
     */
    private static String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }
}
