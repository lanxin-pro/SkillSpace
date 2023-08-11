package cn.iocoder.educate.module.system.convert.sms;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.SmsChannelCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.SmsChannelRespVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.SmsChannelSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.SmsChannelUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信渠道 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/5/28 19:35
 */
@Mapper
public interface SmsChannelConvert {

    SmsChannelConvert INSTANCE = Mappers.getMapper(SmsChannelConvert.class);

    SmsChannelDO convert(SmsChannelCreateReqVO bean);

    SmsChannelDO convert(SmsChannelUpdateReqVO bean);

    List<SmsChannelProperties> convertListToProperties(List<SmsChannelDO> list);

    SmsChannelRespVO convert(SmsChannelDO bean);

    PageResult<SmsChannelRespVO> convertPage(PageResult<SmsChannelDO> page);

    List<SmsChannelSimpleRespVO> convertList03(List<SmsChannelDO> list);

}
