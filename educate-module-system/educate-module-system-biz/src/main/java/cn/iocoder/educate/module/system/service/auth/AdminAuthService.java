package cn.iocoder.educate.module.system.service.auth;

import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;

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
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

}
