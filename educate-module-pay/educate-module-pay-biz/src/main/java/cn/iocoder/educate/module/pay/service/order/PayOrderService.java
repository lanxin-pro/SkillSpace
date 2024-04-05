package cn.iocoder.educate.module.pay.service.order;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderExportReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderPageReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderSubmitReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderExtensionDO;

import java.util.List;

/**
 * 支付订单 Service 接口
 *
 * @author j-sentinel
 * @date 2024/3/27 19:23
 */
public interface PayOrderService {

    /**
     * 获得支付订单
     *
     * @param id 编号
     * @return 支付订单
     */
    PayOrderDO getOrder(Long id);

    /**
     * 获得支付订单
     *
     * @param id 编号
     * @return 支付订单
     */
    PayOrderExtensionDO getOrderExtension(Long id);

    /**
     * 提交支付
     * 此时，会发起支付渠道的调用
     *
     * @param reqVO  提交请求
     * @param userIp 提交 IP
     * @return 提交结果
     */
    PayOrderSubmitRespVO submitOrder(PayOrderSubmitReqVO reqVO, String userIp);

    /**
     * 获得支付订单分页
     *
     * @param pageReqVO 分页查询
     * @return 支付订单分页
     */
    PageResult<PayOrderDO> getOrderPage(PayOrderPageReqVO pageReqVO);

    /**
     * 获得支付订单列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 支付订单列表
     */
    List<PayOrderDO> getOrderList(PayOrderExportReqVO exportReqVO);
}
