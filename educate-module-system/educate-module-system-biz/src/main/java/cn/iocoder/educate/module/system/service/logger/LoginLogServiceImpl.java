package cn.iocoder.educate.module.system.service.logger;

import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:46
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {
    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {

    }
}
