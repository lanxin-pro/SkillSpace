package cn.iocoder.educate.module.pay.dal.mysql.order;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.educate.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderExportReqVO;
import cn.iocoder.educate.module.pay.controller.admin.order.vo.PayOrderPageReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/3/27 19:40
 */
@Mapper
public interface PayOrderMapper extends BaseMapperX<PayOrderDO> {

    default PageResult<PayOrderDO> selectPage(PayOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayOrderDO>()
                .eqIfPresent(PayOrderDO::getAppId, reqVO.getAppId())
                .eqIfPresent(PayOrderDO::getChannelCode, reqVO.getChannelCode())
                .likeIfPresent(PayOrderDO::getMerchantOrderId, reqVO.getMerchantOrderId())
                .likeIfPresent(PayOrderDO::getChannelOrderNo, reqVO.getChannelOrderNo())
                .likeIfPresent(PayOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(PayOrderDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PayOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayOrderDO::getId)
        );
    }

    default List<PayOrderDO> selectList(PayOrderExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PayOrderDO>()
                .eqIfPresent(PayOrderDO::getAppId, reqVO.getAppId())
                .eqIfPresent(PayOrderDO::getChannelCode, reqVO.getChannelCode())
                .likeIfPresent(PayOrderDO::getMerchantOrderId, reqVO.getMerchantOrderId())
                .likeIfPresent(PayOrderDO::getChannelOrderNo, reqVO.getChannelOrderNo())
                .likeIfPresent(PayOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(PayOrderDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PayOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayOrderDO::getId));
    }

}
