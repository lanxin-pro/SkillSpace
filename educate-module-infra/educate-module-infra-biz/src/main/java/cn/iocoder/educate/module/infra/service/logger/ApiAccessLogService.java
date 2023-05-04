package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:21
 * API 访问日志 Service 接口
 */
public interface ApiAccessLogService {

    /**
     * 创建 API 访问日志
     *
     * @param createReqDTO API 访问日志
     */
    void createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO);
}
