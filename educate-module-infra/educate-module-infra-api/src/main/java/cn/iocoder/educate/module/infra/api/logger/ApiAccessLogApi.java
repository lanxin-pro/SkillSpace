package cn.iocoder.educate.module.infra.api.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 10:53
 */
public interface ApiAccessLogApi {

    void createApiAccessLog(ApiAccessLogCreateReqDTO apiAccessLogCreateReqDTO);
}
