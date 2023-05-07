package cn.iocoder.educate.module.infra.api.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 10:53
 */
public interface ApiAccessLogApi {

    /**
     * 创建 API 访问日志
     * @param apiAccessLogCreateReqDTO 创建信息
     */
    void createApiAccessLog(ApiAccessLogCreateReqDTO apiAccessLogCreateReqDTO);
}
