package cn.iocoder.educate.framework.operatelog.core.service;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 14:56
 * 操作日志 Framework Service 接口
 */
public interface OperateLogFrameworkService {

    /**
     * 记录操作日志
     *
     * @param operateLog 操作日志请求
     */
    void createOperateLog(OperateLog operateLog);
}
