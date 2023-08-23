package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.educate.module.infra.covert.logger.ApiErrorLogConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.iocoder.educate.module.infra.dal.mysql.logger.ApiErrorLogMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.infra.enums.logger.ApiErrorLogProcessStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:08
 * API 错误日志 Service 实现类
 */
@Service
@Validated
public class ApiErrorLogServiceImpl implements ApiErrorLogService{

    @Resource
    private ApiErrorLogMapper apiErrorLogMapper;

    @Override
    public void createApiErrorLog(ApiErrorLogCreateReqDTO apiErrorLogCreateReqDTO) {
        ApiErrorLogDO apiErrorLog = ApiErrorLogConvert.INSTANCE.convert(apiErrorLogCreateReqDTO)
                .setProcessStatus(ApiErrorLogProcessStatusEnum.INIT.getStatus());
        apiErrorLogMapper.insert(apiErrorLog);
    }

    @Override
    public PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO apiErrorLogPageReqVO) {
        return apiErrorLogMapper.selectPage(apiErrorLogPageReqVO);
    }

    @Override
    public void updateApiErrorLogProcess(Long id, Integer processStatus, Long processUserId) {
        ApiErrorLogDO errorLog = apiErrorLogMapper.selectById(id);
        if (errorLog == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.API_ERROR_LOG_NOT_FOUND);
        }
        if (!ApiErrorLogProcessStatusEnum.INIT.getStatus().equals(errorLog.getProcessStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.API_ERROR_LOG_PROCESSED);
        }
        // 标记处理
        apiErrorLogMapper.updateById(ApiErrorLogDO.builder().id(id).processStatus(processStatus)
                .processUserId(processUserId).processTime(LocalDateTime.now()).build());
    }
}
