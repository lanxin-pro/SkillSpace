package cn.iocoder.educate.module.system.convert.sms;

import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
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

    List<SmsChannelProperties> convertListToProperties(List<SmsChannelDO> list);
}
