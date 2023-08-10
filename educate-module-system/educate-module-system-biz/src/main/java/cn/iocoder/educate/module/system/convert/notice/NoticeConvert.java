package cn.iocoder.educate.module.system.convert.notice;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticeRespVO;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticeUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.notice.NoticeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/10 18:13
 */
@Mapper
public interface NoticeConvert {

    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    PageResult<NoticeRespVO> convertPage(PageResult<NoticeDO> page);

    NoticeRespVO convert(NoticeDO bean);

    NoticeDO convert(NoticeUpdateReqVO bean);

    NoticeDO convert(NoticeCreateReqVO bean);

}
