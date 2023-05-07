package cn.iocoder.educate.module.system.service.logger;

import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:07
 * 操作日志 Service 接口
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 操作日志请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);
}
