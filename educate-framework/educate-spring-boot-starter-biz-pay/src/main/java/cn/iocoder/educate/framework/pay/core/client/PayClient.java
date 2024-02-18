package cn.iocoder.educate.framework.pay.core.client;

/**
 * 支付客户端，用于对接各支付渠道的 SDK，实现发起支付、退款等功能
 *
 * @author j-sentinel
 * @date 2024/2/18 14:37
 */
public interface PayClient {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

}
