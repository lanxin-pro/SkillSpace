package cn.iocoder.educate.module.infra.api.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.educate.module.infra.service.logger.ApiErrorLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 18:07
 * API 访问日志的 API 接口
 */
@Service
@Validated
public class ApiErrorLogApiImpl implements ApiErrorLogApi{

    @Resource
    private ApiErrorLogService apiErrorLogService;

    @Override
    public void createApiErrorLog(ApiErrorLogCreateReqDTO apiErrorLogCreateReqDTO) {
        apiErrorLogService.createApiErrorLog(apiErrorLogCreateReqDTO);
    }
}
