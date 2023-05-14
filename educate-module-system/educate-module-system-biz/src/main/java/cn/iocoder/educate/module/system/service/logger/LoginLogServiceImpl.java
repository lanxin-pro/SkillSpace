package cn.iocoder.educate.module.system.service.logger;

import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.educate.module.system.convert.logger.LoginLogConvert;
import cn.iocoder.educate.module.system.dal.dataobject.logger.LoginLogDO;
import cn.iocoder.educate.module.system.dal.mysql.logger.LoginLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:46
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {
        LoginLogDO loginLogDO = LoginLogConvert.INSTANCE.convert(reqDTO);
        loginLogMapper.insert(loginLogDO);
    }
}
