package cn.iocoder.educate.module.system.convert.dept;

import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
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
}
