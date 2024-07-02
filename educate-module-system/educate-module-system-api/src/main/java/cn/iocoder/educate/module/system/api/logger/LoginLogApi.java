package cn.iocoder.educate.module.system.api.logger;

import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;

import javax.validation.Valid;

/**
 * 登录日志的 API 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/6 21:15
 */
public interface LoginLogApi {

    /**
     * 创建登录日志
     *
     * @param reqDTO 日志信息
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
