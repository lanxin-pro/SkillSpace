package cn.iocoder.educate.module.system.convert.mail;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateRespVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 13:22
 */
@Mapper
public interface MailTemplateConvert {

    MailTemplateConvert INSTANCE = Mappers.getMapper(MailTemplateConvert.class);

    MailTemplateDO convert(MailTemplateUpdateReqVO bean);

    MailTemplateDO convert(MailTemplateCreateReqVO bean);

    MailTemplateRespVO convert(MailTemplateDO bean);

    PageResult<MailTemplateRespVO> convertPage(PageResult<MailTemplateDO> pageResult);

    List<MailTemplateSimpleRespVO> convertList02(List<MailTemplateDO> list);

}
