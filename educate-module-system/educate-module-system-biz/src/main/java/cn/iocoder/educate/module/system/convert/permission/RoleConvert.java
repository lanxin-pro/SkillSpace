package cn.iocoder.educate.module.system.convert.permission;

import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleRespVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/9 18:00
 */
@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    RoleRespVO convert(RoleDO bean);

    RoleDO convert(RoleCreateReqVO bean);

    RoleDO convert(RoleUpdateReqVO bean);

    List<RoleSimpleRespVO> convertList02(List<RoleDO> list);

}
