package cn.iocoder.educate.module.pay.convert.app;

import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageItemRespVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 支付应用信息 Convert
 *
 * @author j-sentinel
 * @date 2024/2/22 12:22
 */
@Mapper
public interface PayAppConvert {

    PayAppConvert INSTANCE = Mappers.getMapper(PayAppConvert.class);

    PayAppPageItemRespVO convert(PayAppDO payAppDO);

    PageResult<PayAppPageItemRespVO> convertPage(PageResult<PayAppDO> page);

    default PageResult<PayAppPageItemRespVO> convertPage(PageResult<PayAppDO> pageResult, List<PayChannelDO> channels) {
        PageResult<PayAppPageItemRespVO> voPageResult = convertPage(pageResult);
        Set<String> collect = channels.stream().map(PayChannelDO::getCode).collect(Collectors.toSet());
        voPageResult.getList().forEach(app ->
                app.setChannelCodes(collect));
        return voPageResult;
    }

}
