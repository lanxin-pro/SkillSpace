package cn.iocoder.educate.module.system.service.social;

import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.module.system.enums.social.SocialTypeEnum;

/**
 * 社交用户 Service 接口，例如说社交平台的授权登录
 *
 * @Author: 董伟豪
 * @Date: 2023/6/7 9:10
 */
public interface SocialUserService {

    /**
     * 获得社交平台的授权 URL
     *
     * @param type 社交平台的类型 {@link SocialTypeEnum}
     * @param redirectUri 重定向 URL
     * @return 社交平台的授权 URL
     */
    String getAuthorizeUrl(Integer type, String redirectUri);

    /**
     * 获得社交用户的绑定用户编号
     * 注意，返回的是 MemberUser 或者 AdminUser 的 id 编号！
     * 在认证信息不正确的情况下，也会抛出 {@link ServiceException} 业务异常
     *
     * @param value 用户类型
     * @param type 社交平台的类型
     * @param code 授权码
     * @param state state
     * @return 绑定用户编号
     */
    Long getBindUserId(Integer value, Integer type, String code, String state);
}
