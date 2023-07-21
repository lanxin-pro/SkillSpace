package cn.iocoder.educate.module.system.convert.permission;

import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuRespVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuSimpleRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 17:16
 */
@Mapper
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    List<MenuRespVO> convertList(List<MenuDO> list);

    MenuDO convert(MenuCreateReqVO bean);

    List<MenuSimpleRespVO> convertList02(List<MenuDO> list);

}
