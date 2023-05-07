package cn.iocoder.educate.module.system.api.logger;

import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;

import javax.validation.Valid;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 14:57
 * 操作日志 API 接口
 */
public interface OperateLogApi {

    /**
     * 创建操作日志
     *
     * @param createReqDTO 请求
     */
    void createOperateLog(@Valid OperateLogCreateReqDTO createReqDTO);
}
