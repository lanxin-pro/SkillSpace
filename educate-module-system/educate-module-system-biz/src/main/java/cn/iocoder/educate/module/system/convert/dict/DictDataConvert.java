package cn.iocoder.educate.module.system.convert.dict;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataRespVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:22
 */
@Mapper
public interface DictDataConvert {

    DictDataConvert INSTANCE = Mappers.getMapper(DictDataConvert.class);

    List<DictDataSimpleRespVO> convertList(List<DictDataDO> list);

    DictDataRespVO convert(DictDataDO bean);

    PageResult<DictDataRespVO> convertPage(PageResult<DictDataDO> page);

    DictDataDO convert(DictDataUpdateReqVO bean);

    DictDataDO convert(DictDataCreateReqVO bean);

}
