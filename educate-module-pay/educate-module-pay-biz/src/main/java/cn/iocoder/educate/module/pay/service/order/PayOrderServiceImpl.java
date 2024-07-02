package cn.iocoder.educate.module.pay.service.order;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderExportReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderPageReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderSubmitReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.educate.module.pay.dal.mysql.order.PayOrderExtensionMapper;
import cn.iocoder.educate.module.pay.dal.mysql.order.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付订单 Service 实现类
 *
 * @author j-sentinel
 * @date 2024/3/27 19:38
 */
@Slf4j
@Service
@Validated
public class PayOrderServiceImpl implements PayOrderService {

    @Resource
    private PayOrderMapper orderMapper;

    @Resource
    private PayOrderExtensionMapper orderExtensionMapper;

    @Override
    public PayOrderDO getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PayOrderExtensionDO getOrderExtension(Long id) {
        return orderExtensionMapper.selectById(id);
    }

    /**
     * 注意，这里不能添加事务注解，避免调用支付渠道失败时，将 PayOrderExtensionDO 回滚了，支付订单不需要回滚
     *
     * @param reqVO  提交请求
     * @param userIp 提交 IP
     * @return
     */
    @Override
    public PayOrderSubmitRespVO submitOrder(PayOrderSubmitReqVO reqVO, String userIp) {
        return null;
    }

    @Override
    public PageResult<PayOrderDO> getOrderPage(PayOrderPageReqVO pageReqVO) {
        return orderMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PayOrderDO> getOrderList(PayOrderExportReqVO exportReqVO) {
        return orderMapper.selectList(exportReqVO);
    }

}
