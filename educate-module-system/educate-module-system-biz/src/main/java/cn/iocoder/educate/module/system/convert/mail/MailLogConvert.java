package cn.iocoder.educate.module.system.convert.mail;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.log.MailLogRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 16:26
 */
@Mapper
public interface MailLogConvert {

    MailLogConvert INSTANCE = Mappers.getMapper(MailLogConvert.class);

    MailLogRespVO convert(MailLogDO bean);

    PageResult<MailLogRespVO> convertPage(PageResult<MailLogDO> pageResult);

}
