package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.educate.module.infra.covert.logger.ApiErrorLogConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.iocoder.educate.module.infra.dal.mysql.logger.ApiErrorLogMapper;
import cn.iocoder.educate.module.infra.enums.logger.ApiErrorLogProcessStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

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
}
