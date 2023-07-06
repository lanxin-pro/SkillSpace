package cn.iocoder.educate.module.system.service.logger;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:07
 * 操作日志 Service 接口
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 操作日志请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param reqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO reqVO);
}
