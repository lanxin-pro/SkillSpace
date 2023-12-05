package cn.iocoder.educate.module.member.service.auth;

import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsLoginReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员的认证 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/5 20:47
 */
@Service
@Slf4j
public class MemberAuthServiceImpl implements MemberAuthService {

    @Override
    public AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO appAuthSmsLoginReqVO, Integer terminal) {
        return null;
    }

}
