package cn.iocoder.educate.module.system.convert.notify;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessageRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyMessageDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/14 12:54
 */
@Mapper
public interface NotifyMessageConvert {

    NotifyMessageConvert INSTANCE = Mappers.getMapper(NotifyMessageConvert.class);

    NotifyMessageRespVO convert(NotifyMessageDO bean);

    List<NotifyMessageRespVO> convertList(List<NotifyMessageDO> list);

    PageResult<NotifyMessageRespVO> convertPage(PageResult<NotifyMessageDO> page);

}
