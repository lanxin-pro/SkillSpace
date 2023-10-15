package cn.iocoder.educate.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.dal.mysql.oauth2.OAuth2ApproveMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/9 10:09
 */
@Slf4j
@Service
public class OAuth2ApproveServiceImpl implements OAuth2ApproveService {

    @Resource
    private OAuth2ApproveMapper oauth2ApproveMapper;

    @Resource
    private OAuth2ClientService oAuth2ClientService;

    /**
     * 批准的过期时间，默认 30 天
     *
     * 单位秒
     */
    private static final Integer TIMEOUT = 30 * 24 * 60 * 60;

    @Override
    public List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId) {
        List<OAuth2ApproveDO> approveDOs = oauth2ApproveMapper.selectListByUserIdAndUserTypeAndClientId(
                userId, userType, clientId);
        // 去除过期时间
        approveDOs.removeIf(o -> DateUtils.isExpired(o.getExpiresTime()));
        return approveDOs;
    }

    @Override
    public boolean checkForPreApproval(Long userId, Integer userType, String clientId, Set<String> requestedScopes) {
        // 第一步，基于 Client 的自动授权计算，如果 scopes 都在自动授权中，则返回 true 通过
        OAuth2ClientDO oAuth2ClientDO = oAuth2ClientService.validOAuthClientFromCache(clientId);
        // 防御性编程
        Assert.notNull(oAuth2ClientDO, "客户端不能为空");
        // 匹配自动审批
        if (CollUtil.containsAll(oAuth2ClientDO.getAutoApproveScopes(), requestedScopes)) {
            // if all scopes are auto approved, approvals still need to be added to the approval store.
            // 如果所有范围都是自动审批的，则仍然需要将审批添加到审批存储中。
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
            for (String scope : requestedScopes) {
                saveApprove(userId, userType, clientId, scope, true, expireTime);
            }
            return true;
        }
        // 第二步，算上用户已经批准的授权。如果 scopes 都包含，则返回 true
        List<OAuth2ApproveDO> approveDOs = getApproveList(userId, userType, clientId);
        // 只保留未过期的 + 同意的
        Set<String> scopes = approveDOs.stream()
                .filter(OAuth2ApproveDO::getApproved)
                .map(OAuth2ApproveDO::getScope)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return CollUtil.containsAll(scopes, requestedScopes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAfterApproval(Long userId, Integer userType, String clientId,
                                       Map<String, Boolean> requestedScopes) {
        // 如果 requestedScopes 为空，说明没有要求，则返回 true 通过
        if (CollUtil.isEmpty(requestedScopes)) {
            return true;
        }
        // 更新批准的信息
        // 需要至少有一个同意
        boolean success = false;
        // 计算过期时间
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        for (Map.Entry<String, Boolean> entry : requestedScopes.entrySet()) {
            if (entry.getValue()) {
                success = true;
            }
            saveApprove(userId, userType, clientId, entry.getKey(), entry.getValue(), expireTime);
        }
        // 至少有一个同意的话就会返回false
        return success;
    }

    private void saveApprove(Long userId, Integer userType, String clientId, String scope, boolean approved,
                             LocalDateTime expireTime) {
        // 先更新
        OAuth2ApproveDO approveDO = new OAuth2ApproveDO().setUserId(userId).setUserType(userType)
                .setClientId(clientId).setScope(scope).setApproved(approved).setExpiresTime(expireTime);
        if (oauth2ApproveMapper.update(approveDO) == 1) {
            return;
        }
        // 失败，则说明不存在，进行添加
        oauth2ApproveMapper.insert(approveDO);
    }

}
