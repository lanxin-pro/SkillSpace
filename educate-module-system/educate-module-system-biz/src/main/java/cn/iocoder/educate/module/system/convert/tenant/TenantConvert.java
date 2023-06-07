package cn.iocoder.educate.module.system.convert.tenant;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户 Convert
 *
 * @Author: 董伟豪
 * @Date: 2023/6/6 16:42
 */
@Mapper
public interface TenantConvert {

    TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);
}
