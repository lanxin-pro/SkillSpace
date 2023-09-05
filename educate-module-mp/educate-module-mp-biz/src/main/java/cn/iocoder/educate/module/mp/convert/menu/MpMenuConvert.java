package cn.iocoder.educate.module.mp.convert.menu;

import cn.iocoder.educate.module.mp.controller.admin.menu.vo.MpMenuRespVO;
import cn.iocoder.educate.module.mp.controller.admin.menu.vo.MpMenuSaveReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.menu.MpMenuDO;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/5 16:28
 */
@Mapper
public interface MpMenuConvert {

    MpMenuConvert INSTANCE = Mappers.getMapper(MpMenuConvert.class);

    MpMenuRespVO convert(MpMenuDO bean);

    List<MpMenuRespVO> convertList(List<MpMenuDO> list);

    @Mappings({
            @Mapping(source = "menuKey", target = "key"),
            @Mapping(source = "children", target = "subButtons"),
    })
    List<WxMenuButton> convert(List<MpMenuSaveReqVO.Menu> list);

    MpMenuDO convert02(MpMenuSaveReqVO.Menu menu);

}
