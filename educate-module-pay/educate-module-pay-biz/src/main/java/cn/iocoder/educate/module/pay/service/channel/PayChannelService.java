package cn.iocoder.educate.module.pay.service.channel;

import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;

import java.util.List;
import java.util.Set;

/**
 * 支付渠道 Service 接口
 *
 * @author j-sentinel
 * @date 2024/2/18 15:04
 */
public interface PayChannelService {

    /**
     * 根据支付应用 ID 集合，获得支付渠道列表
     *
     * @param appIds 应用编号集合
     * @return 支付渠道列表
     */
    List<PayChannelDO> getChannelListByAppIds(Set<Long> appIds);
}
