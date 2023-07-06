package cn.iocoder.educate.module.system.convert.logger;

import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:10
 */
@Mapper
public interface OperateLogConvert {

    OperateLogConvert INSTANCE = Mappers.getMapper(OperateLogConvert.class);

    OperateLogDO convert(OperateLogCreateReqDTO operateLogCreateReqDTO);

    OperateLogRespVO convert(OperateLogDO bean);
}
