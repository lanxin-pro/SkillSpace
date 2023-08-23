package cn.iocoder.educate.module.infra.covert.logger;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:12
 */
@Mapper
public interface ApiErrorLogConvert {

    ApiErrorLogConvert INSTANCE = Mappers.getMapper(ApiErrorLogConvert.class);

    ApiErrorLogDO convert(ApiErrorLogCreateReqDTO apiErrorLogCreateReqDTO);

    ApiErrorLogRespVO convert(ApiErrorLogDO bean);

    PageResult<ApiErrorLogRespVO> convertPage(PageResult<ApiErrorLogDO> page);

}
