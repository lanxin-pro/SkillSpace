package cn.iocoder.educate.module.system.convert.user;

import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageItemRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 12:01
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserPageItemRespVO convert(AdminUserDO bean);

    UserPageItemRespVO.Dept convert(DeptDO bean);
}
