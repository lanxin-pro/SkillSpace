package cn.iocoder.educate.module.system.convert.mail;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.*;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/18 13:38
 */
@Mapper
public interface MailAccountConvert {

    MailAccountConvert INSTANCE = Mappers.getMapper(MailAccountConvert.class);

    MailAccountDO convert(MailAccountCreateReqVO bean);

    MailAccountDO convert(MailAccountUpdateReqVO bean);

    MailAccountRespVO convert(MailAccountDO bean);

    PageResult<MailAccountBaseVO> convertPage(PageResult<MailAccountDO> pageResult);

    List<MailAccountSimpleRespVO> convertList02(List<MailAccountDO> list);


}
