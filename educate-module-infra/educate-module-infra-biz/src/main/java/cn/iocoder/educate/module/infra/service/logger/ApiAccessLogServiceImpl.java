package cn.iocoder.educate.module.infra.service.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.educate.module.infra.covert.logger.ApiAccessLogConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import cn.iocoder.educate.module.infra.dal.mysql.logger.ApiAccessLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:21
 */
@Service
@Validated
public class ApiAccessLogServiceImpl implements ApiAccessLogService{

    @Resource
    private ApiAccessLogMapper apiAccessLogMapper;

    @Override
    public void createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO) {
        ApiAccessLogDO apiAccessLogDO = ApiAccessLogConvert.INSTANCE.convert(createReqDTO);
        apiAccessLogMapper.insert(apiAccessLogDO);
    }
}
