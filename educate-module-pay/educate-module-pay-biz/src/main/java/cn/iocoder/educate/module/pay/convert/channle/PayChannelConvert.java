package cn.iocoder.educate.module.pay.convert.channle;

import cn.iocoder.educate.framework.pay.core.client.PayClientConfig;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelCreateReqVO;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelRespVO;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelUpdateReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author j-sentinel
 * @date 2024/2/24 12:43
 */
@Mapper
public interface PayChannelConvert {

    PayChannelConvert INSTANCE = Mappers.getMapper(PayChannelConvert.class);

    /**
     * String对象无法映射 -> DO中的${@link PayClientConfig } 中的config，目前只能忽略掉，只能靠后续的代码来处理
     *
     * @param bean
     * @return
     */
    @Mapping(target = "config",ignore = true)
    PayChannelDO convert(PayChannelCreateReqVO bean);

    @Mapping(target = "config",ignore = true)
    PayChannelDO convert(PayChannelUpdateReqVO bean);

    @Mapping(target = "config",expression = "java(cn.iocoder.educate.framework.common.util.json.JsonUtils.toJsonString(bean.getConfig()))")
    PayChannelRespVO convert(PayChannelDO bean);

}
