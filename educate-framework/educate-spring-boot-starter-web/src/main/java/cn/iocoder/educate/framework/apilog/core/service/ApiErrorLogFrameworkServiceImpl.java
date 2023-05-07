package cn.iocoder.educate.framework.apilog.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.infra.api.logger.ApiErrorLogApi;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 18:03
 */
@RequiredArgsConstructor
public class ApiErrorLogFrameworkServiceImpl implements ApiErrorLogFrameworkService{

    private final ApiErrorLogApi apiErrorLogApi;

    @Override
    @Async
    public void createApiErrorLog(ApiErrorLog apiErrorLog) {
        ApiErrorLogCreateReqDTO reqDTO = BeanUtil.copyProperties(apiErrorLog, ApiErrorLogCreateReqDTO.class);
        apiErrorLogApi.createApiErrorLog(reqDTO);
    }
}
