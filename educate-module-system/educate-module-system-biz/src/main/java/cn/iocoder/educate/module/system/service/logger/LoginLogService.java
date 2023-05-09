package cn.iocoder.educate.module.system.service.logger;

import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/9 8:46
 * 登录日志 Service 接口
 */
public interface LoginLogService {

    void createLoginLog(LoginLogCreateReqDTO reqDTO);
}
