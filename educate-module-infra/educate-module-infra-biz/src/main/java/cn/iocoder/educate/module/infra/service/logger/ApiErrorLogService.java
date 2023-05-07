package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:08
 * API 错误日志 Service 接口
 */
public interface ApiErrorLogService {

    /**
     * 创建 API 错误日志
     *
     * @param createReqDTO API 错误日志
     */
    void createApiErrorLog(ApiErrorLogCreateReqDTO createReqDTO);

}
