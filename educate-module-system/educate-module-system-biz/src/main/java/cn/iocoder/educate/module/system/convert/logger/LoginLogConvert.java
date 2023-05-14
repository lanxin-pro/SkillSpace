package cn.iocoder.educate.module.system.convert.logger;

import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.educate.module.system.dal.dataobject.logger.LoginLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/12 21:18
 */
@Mapper
public interface LoginLogConvert {

    LoginLogConvert INSTANCE = Mappers.getMapper(LoginLogConvert.class);

    LoginLogDO convert(LoginLogCreateReqDTO loginLogCreateReqDTO);
}
