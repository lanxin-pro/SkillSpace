package cn.iocoder.educate.framework.apilog.core.service;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 16:59
 * API 错误日志 Framework Service 接口
 */
public interface ApiErrorLogFrameworkService {

    /**
     * 创建 API 错误日志
     *
     * @param apiErrorLog API 错误日志
     */
    void createApiErrorLog(ApiErrorLog apiErrorLog);
}
