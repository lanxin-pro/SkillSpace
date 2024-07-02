package cn.iocoder.educate.module.pay.convert.order;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderDetailsRespVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderExcelVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderPageItemRespVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderRespVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付订单 Convert
 *
 * @author j-sentinel
 * @date 2024/3/27 19:33
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderRespVO convert(PayOrderDO bean);

    default PayOrderDetailsRespVO convert(PayOrderDO order, PayOrderExtensionDO orderExtension, PayAppDO app) {
        PayOrderDetailsRespVO payOrderDetailsRespVO = convertDetail(order);
        payOrderDetailsRespVO.setExtension(convert(orderExtension));
        if (app != null) {
            payOrderDetailsRespVO.setAppName(app.getName());
        }
        return payOrderDetailsRespVO;
    }

    PageResult<PayOrderPageItemRespVO> convertPage(PageResult<PayOrderDO> page);

    default PageResult<PayOrderPageItemRespVO> convertPage(PageResult<PayOrderDO> page, Map<Long, PayAppDO> appMap) {
        PageResult<PayOrderPageItemRespVO> result = convertPage(page);
        result.getList().forEach(order -> MapUtils.findAndThen(appMap,
                order.getAppId(),
                app -> order.setAppName(app.getName())));
        return result;
    }

    PayOrderDetailsRespVO convertDetail(PayOrderDO bean);

    PayOrderDetailsRespVO.PayOrderExtension convert(PayOrderExtensionDO bean);

    PayOrderExcelVO convertExcel(PayOrderDO bean);

    default List<PayOrderExcelVO> convertList(List<PayOrderDO> list, Map<Long, PayAppDO> appMap) {
        return list.stream().map(order -> {
            PayOrderExcelVO excelVO = convertExcel(order);
            MapUtils.findAndThen(appMap, order.getAppId(), app -> excelVO.setAppName(app.getName()));
            return excelVO;
        }).collect(Collectors.toList());
    }

}
