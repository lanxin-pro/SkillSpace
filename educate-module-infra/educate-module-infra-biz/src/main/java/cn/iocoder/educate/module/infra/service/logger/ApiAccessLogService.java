package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:21
 * API 访问日志 Service 接口
 */
public interface ApiAccessLogService {

    /**
     * 创建 API 访问日志
     *
     * @param createReqDTO API 访问日志
     */
    void createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO);

    /**
     * 获得 API 访问日志分页
     *
     * @param pageReqVO 分页查询
     * @return API 访问日志分页
     */
    PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO);
}
