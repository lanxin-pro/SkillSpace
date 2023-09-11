package cn.iocoder.educate.module.mp.convert.material;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialRespVO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/11 15:44
 */
@Mapper
public interface MpMaterialConvert {

    MpMaterialConvert INSTANCE = Mappers.getMapper(MpMaterialConvert.class);

    PageResult<MpMaterialRespVO> convertPage(PageResult<MpMaterialDO> page);

}
