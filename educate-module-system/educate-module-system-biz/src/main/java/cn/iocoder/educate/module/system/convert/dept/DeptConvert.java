package cn.iocoder.educate.module.system.convert.dept;

import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptRespVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:40
 */
@Mapper
public interface DeptConvert {

    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    List<DeptSimpleRespVO> convertList02(List<DeptDO> list);

    List<DeptRespVO> convertList(List<DeptDO> list);

    DeptRespVO convert(DeptDO bean);

    DeptDO convert(DeptCreateReqVO bean);

    DeptDO convert(DeptUpdateReqVO bean);

    UserSimpleRespVO convert(AdminUserDO bean);


}
