package cn.iocoder.educate.module.member.service.auth;

import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsLoginReqVO;

/**
 * 会员的认证 Service 接口
 *
 * 提供用户的账号密码登录、token 的校验等认证相关的功能
 *
 * @Author: j-sentinel
 * @Date: 2023/12/5 20:47
 */
public interface MemberAuthService {

    /**
     * 手机 + 验证码登陆
     *
     * @param appAuthSmsLoginReqVO    登陆信息
     * @param terminal 终端 {@link TerminalEnum}
     * @return 登录结果
     */
    AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO appAuthSmsLoginReqVO, Integer terminal);

}
