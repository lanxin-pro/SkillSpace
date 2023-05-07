package cn.iocoder.educate.framework.apilog.core.service;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 10:48
 * API 访问日志 Framework Service 接口
 */
public interface ApiAccessLogFrameworkService {

    /**
     * 创建 API 访问日志
     * @param accessLog API 访问日志
     */
    void createApiAccessLog(ApiAccessLog accessLog);
}
