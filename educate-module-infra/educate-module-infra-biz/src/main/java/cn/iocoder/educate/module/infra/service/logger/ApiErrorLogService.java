package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;

import java.util.List;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:08
 * API 错误日志 Service 接口
 */
public interface ApiErrorLogService {

    /**
     * 创建 API 错误日志
     *
     * @param createReqDTO API 错误日志
     */
    void createApiErrorLog(ApiErrorLogCreateReqDTO createReqDTO);

    /**
     * 获得 API 错误日志分页
     *
     * @param pageReqVO 分页查询
     * @return API 错误日志分页
     */
    PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO pageReqVO);

    /**
     * 更新 API 错误日志已处理
     *
     * @param id API 日志编号
     * @param processStatus 处理结果
     * @param processUserId 处理人
     */
    void updateApiErrorLogProcess(Long id, Integer processStatus, Long processUserId);

}
