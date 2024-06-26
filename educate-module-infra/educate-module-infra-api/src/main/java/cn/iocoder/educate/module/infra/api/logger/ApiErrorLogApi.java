package cn.iocoder.educate.module.infra.api.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import javax.validation.Valid;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:04
 *
 * API 错误日志的 API 接口
 */
public interface ApiErrorLogApi {

    /**
     * 创建 API 错误日志
     *
     * @param createDTO 创建信息
     */
    void createApiErrorLog(@Valid ApiErrorLogCreateReqDTO createDTO);
}
