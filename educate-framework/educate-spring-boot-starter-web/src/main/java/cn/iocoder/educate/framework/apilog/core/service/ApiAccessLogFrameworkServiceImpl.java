package cn.iocoder.educate.framework.apilog.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.infra.api.logger.ApiAccessLogApi;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 10:52
 * API 访问日志 Framework Service 实现类
 *
 * 基于 {@link ApiAccessLogApi} 服务，记录访问日志
 */
@RequiredArgsConstructor
public class ApiAccessLogFrameworkServiceImpl implements ApiAccessLogFrameworkService{

    private final ApiAccessLogApi apiAccessLogApi;

    @Override
    @Async
    public void createApiAccessLog(ApiAccessLog apiAccessLog) {
        ApiAccessLogCreateReqDTO apiAccessLogCreateReqDTO = BeanUtil.copyProperties(apiAccessLog, ApiAccessLogCreateReqDTO.class);
        apiAccessLogApi.createApiAccessLog(apiAccessLogCreateReqDTO);
    }
}
