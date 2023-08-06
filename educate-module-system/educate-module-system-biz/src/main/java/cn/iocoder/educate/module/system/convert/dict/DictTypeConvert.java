package cn.iocoder.educate.module.system.convert.dict;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeRespVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictTypeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/6 10:35
 */
@Mapper
public interface DictTypeConvert {

    DictTypeConvert INSTANCE = Mappers.getMapper(DictTypeConvert.class);

    PageResult<DictTypeRespVO> convertPage(PageResult<DictTypeDO> bean);

    DictTypeRespVO convert(DictTypeDO bean);

    DictTypeDO convert(DictTypeCreateReqVO bean);

    DictTypeDO convert(DictTypeUpdateReqVO bean);

    List<DictTypeSimpleRespVO> convertList(List<DictTypeDO> list);

}
