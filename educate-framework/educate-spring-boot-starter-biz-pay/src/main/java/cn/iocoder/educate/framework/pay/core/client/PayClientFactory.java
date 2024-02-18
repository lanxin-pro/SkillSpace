package cn.iocoder.educate.framework.pay.core.client;

/**
 * @author j-sentinel
 * @date 2024/2/18 14:37
 */
public interface PayClientFactory {

    /**
     * 获得支付客户端
     *
     * @param channelId 渠道编号
     * @return 支付客户端
     */
    PayClient getPayClient(Long channelId);

}
