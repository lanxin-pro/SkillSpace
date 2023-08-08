package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.client.OAuth2ClientCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.client.OAuth2ClientUpdateReqVO;
import cn.iocoder.educate.module.system.convert.oauth2.OAuth2ClientConvert;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import cn.iocoder.educate.module.system.mq.producer.OAuth2ClientProducer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
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

    @Resource
    private OAuth2ClientProducer oAuth2ClientProducer;

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
        // 校验客户端密钥(密钥必须存在才会进行校验后面的逻辑)
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

    @Override
    public Long createOAuth2Client(OAuth2ClientCreateReqVO createReqVO) {
        // 校验client是否重复
        validateClientIdExists(null, createReqVO.getClientId());

        // 插入
        OAuth2ClientDO oauth2Client = OAuth2ClientConvert.INSTANCE.convert(createReqVO);
        oAuth2ClientMapper.insert(oauth2Client);
        // 发送刷新消息
        oAuth2ClientProducer.sendOAuth2ClientRefreshMessage();
        return oauth2Client.getId();
    }

    @Override
    public void updateOAuth2Client(OAuth2ClientUpdateReqVO oAuth2ClientUpdateReqVO) {
        // 校验存在
        validateOAuth2ClientExists(oAuth2ClientUpdateReqVO.getId());
        // 校验 Client 未被占用
        validateClientIdExists(oAuth2ClientUpdateReqVO.getId(), oAuth2ClientUpdateReqVO.getClientId());

        // 更新
        OAuth2ClientDO oAuth2ClientDO = OAuth2ClientConvert.INSTANCE.convert(oAuth2ClientUpdateReqVO);
        oAuth2ClientMapper.updateById(oAuth2ClientDO);
        // 发送刷新消息
        oAuth2ClientProducer.sendOAuth2ClientRefreshMessage();
    }

    @Override
    public void deleteOAuth2Client(Long id) {
        // 校验存在
        validateOAuth2ClientExists(id);
        // 删除
        oAuth2ClientMapper.deleteById(id);
        // 发送刷新消息
        oAuth2ClientProducer.sendOAuth2ClientRefreshMessage();
    }

    @Override
    public OAuth2ClientDO getOAuth2Client(Long id) {
        return oAuth2ClientMapper.selectById(id);
    }

    @Override
    public PageResult<OAuth2ClientDO> getOAuth2ClientPage(OAuth2ClientPageReqVO oAuth2ClientPageReqVO) {
        return oAuth2ClientMapper.selectPage(oAuth2ClientPageReqVO);
    }

    @Override
    public List<Map<String, String>> getClientIds() {
         return clientCache.entrySet().stream()
                .map(entry -> {
                    Long id = entry.getValue().getId();
                    String clientId = entry.getValue().getClientId();
                    Map<String, String> clientData = new HashMap<>();
                    clientData.put("id", id.toString());
                    clientData.put("clientId", clientId);
                    return clientData;
                }).collect(Collectors.toList());
    }

    private void validateOAuth2ClientExists(Long id) {
        if (oAuth2ClientMapper.selectById(id) == null) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
    }

    void validateClientIdExists(Long id, String clientId) {
        OAuth2ClientDO client = clientCache.get(clientId);
        if (client == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的客户端
        if (id == null) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
        if (!client.getId().equals(id)) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
    }

}
