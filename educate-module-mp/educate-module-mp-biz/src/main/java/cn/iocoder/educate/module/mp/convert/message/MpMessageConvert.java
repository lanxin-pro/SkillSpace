package cn.iocoder.educate.module.mp.convert.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageRespVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/6 11:43
 */
@Mapper
public interface MpMessageConvert {

    MpMessageConvert INSTANCE = Mappers.getMapper(MpMessageConvert.class);

    MpMessageRespVO convert(MpMessageDO bean);

    List<MpMessageRespVO> convertList(List<MpMessageDO> list);

    PageResult<MpMessageRespVO> convertPage(PageResult<MpMessageDO> page);

}
