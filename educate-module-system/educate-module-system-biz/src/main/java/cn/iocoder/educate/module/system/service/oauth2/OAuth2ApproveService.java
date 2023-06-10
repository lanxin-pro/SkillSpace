package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import java.util.List;

/**
 * OAuth2 批准 Service 接口
 *
 * 从功能上，和 Spring Security OAuth 的 ApprovalStoreUserApprovalHandler 的功能，记录用户针对指定客户端的授权，减少手动确定。
 *
 * @Author: 董伟豪
 * @Date: 2023/6/9 10:08
 */
public interface OAuth2ApproveService {

    /**
     * 获得用户的批准列表，排除已过期的
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @return 是否授权通过
     */
    List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId);
}
