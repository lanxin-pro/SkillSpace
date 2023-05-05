package cn.iocoder.educate.module.infra.covert.logger;

import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 12:02
 */
@Mapper
public interface ApiAccessLogConvert {

    ApiAccessLogConvert  INSTANCE = Mappers.getMapper(ApiAccessLogConvert.class);

    ApiAccessLogDO convert(ApiAccessLogCreateReqDTO bean);
}
