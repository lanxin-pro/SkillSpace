package cn.iocoder.educate.module.pay.service.channel;

import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelCreateReqVO;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelUpdateReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;

import javax.validation.Valid;
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

    /**
     * 创建支付渠道
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createChannel(@Valid PayChannelCreateReqVO createReqVO);

    /**
     * 更新支付渠道
     *
     * @param updateReqVO 更新信息
     */
    void updateChannel(@Valid PayChannelUpdateReqVO updateReqVO);

    /**
     * 删除支付渠道
     *
     * @param id 编号
     */
    void deleteChannel(Long id);

    /**
     * 获得支付渠道
     *
     * @param id 编号
     * @return 支付渠道
     */
    PayChannelDO getChannel(Long id);

    /**
     * 根据条件获取渠道
     *
     * @param appId      应用编号
     * @param code       渠道编码
     * @return 数量
     */
    PayChannelDO getChannelByAppIdAndCode(Long appId, String code);

    /**
     * 获得指定应用的开启的渠道列表
     *
     * @param appId 应用编号
     * @return 渠道列表
     */
    List<PayChannelDO> getEnableChannelList(Long appId);
}
