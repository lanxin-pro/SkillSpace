package cn.iocoder.educate.module.pay.dal.mysql.channel;

import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author j-sentinel
 * @date 2024/2/18 15:12
 */
@Mapper
public interface PayChannelMapper extends BaseMapper<PayChannelDO> {

    default List<PayChannelDO> selectListByAppIds(Set<Long> appIds) {
        LambdaQueryWrapper<PayChannelDO> payChannelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payChannelDOLambdaQueryWrapper.in(PayChannelDO::getId, appIds);
        return this.selectList(payChannelDOLambdaQueryWrapper);
    }

    default PayChannelDO selectByAppIdAndCode(Long appId, String code) {
        LambdaQueryWrapper<PayChannelDO> payChannelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payChannelDOLambdaQueryWrapper.eq(PayChannelDO::getAppId, appId).eq(PayChannelDO::getCode, code);
        return this.selectOne(payChannelDOLambdaQueryWrapper);
    }

    default List<PayChannelDO> selectListByAppId(Long appId, Integer status) {
        LambdaQueryWrapper<PayChannelDO> payChannelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payChannelDOLambdaQueryWrapper.eq(PayChannelDO::getAppId, appId)
                .eq(PayChannelDO::getStatus, status);
        return this.selectList(payChannelDOLambdaQueryWrapper);
    }
}
