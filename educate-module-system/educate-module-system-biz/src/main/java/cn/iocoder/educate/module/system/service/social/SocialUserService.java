package cn.iocoder.educate.module.system.service.social;

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
}
