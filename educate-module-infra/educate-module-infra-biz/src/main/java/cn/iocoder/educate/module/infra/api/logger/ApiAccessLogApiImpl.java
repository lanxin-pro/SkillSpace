package cn.iocoder.educate.module.infra.api.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.educate.module.infra.service.logger.ApiAccessLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:08
 */
@Service
@Validated
public class ApiAccessLogApiImpl implements ApiAccessLogApi{

    @Resource
    private ApiAccessLogService apiAccessLogService;

    @Override
    public void createApiAccessLog(ApiAccessLogCreateReqDTO apiAccessLogCreateReqDTO) {

    }
}
