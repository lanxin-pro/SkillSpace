package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.educate.module.system.enums.ErrorCodeConstants.*;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:39
 */
@Slf4j
@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService{

    @Resource
    private OAuth2ClientMapper oAuth2ClientMapper;

    /**
     * 客户端缓存
     *
     * key：客户端编号 {@link OAuth2ClientDO#getClientId()} ()}
     *
     * 禁止线程的工作内存对volatile修饰的变量进行缓存
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Map<String, OAuth2ClientDO> clientCache;

    /**
     * 初始化 {@link #clientCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<OAuth2ClientDO> clients = oAuth2ClientMapper.selectList(new QueryWrapper<>());
        log.info("[initLocalCache][缓存 OAuth2 客户端，数量为:{}]", clients.size());

        // 第二步：构建缓存。Function.identity()返回一个总是返回其输入参数的函数
        clientCache = clients.stream().collect(Collectors.toMap(OAuth2ClientDO::getClientId,
                Function.identity(),(oldValue, newValue) -> newValue));

    }

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
        // 校验客户端密钥
        if (StrUtil.isNotEmpty(clientSecret) && ObjectUtil.notEqual(oAuth2ClientDO.getSecret(), clientSecret)) {
            throw exception(OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }
        // 校验授权方式
        if (StrUtil.isNotEmpty(authorizedGrantType) && !CollUtil.contains(oAuth2ClientDO.getAuthorizedGrantTypes(), authorizedGrantType)) {
            throw exception(OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }
        // 校验授权范围
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(oAuth2ClientDO.getScopes(), scopes)) {
            throw exception(OAUTH2_CLIENT_SCOPE_OVER);
        }


        return oAuth2ClientDO;
    }
}
