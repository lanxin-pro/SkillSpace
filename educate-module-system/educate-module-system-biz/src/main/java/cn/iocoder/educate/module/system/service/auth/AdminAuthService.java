package cn.iocoder.educate.module.system.service.auth;

import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSocialLoginReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;

import javax.validation.Valid;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/8 20:05
 *
 * 管理后台的认证 Service 接口
 *
 * 提供用户的登录、登出的能力
 */
public interface AdminAuthService {

    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    AdminUserDO authenticate(String username, String password);

    /**
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * 短信验证码发送
     *
     * @param reqVO 发送请求
     */
    void sendSmsCode(AuthSmsSendReqVO reqVO);

    /**
     * 社交快捷登录，使用 code 授权码
     *
     * @param reqVO
     * @return
     */
    AuthLoginRespVO socialLogin(@Valid AuthSocialLoginReqVO reqVO);
}
