package cn.iocoder.educate.framework.pay.core.enums.channel;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.educate.framework.pay.core.client.PayClientConfig;
import cn.iocoder.educate.framework.pay.core.client.impl.NonePayClientConfig;
import cn.iocoder.educate.framework.pay.core.client.impl.alipay.AlipayPayClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道的编码的枚举
 *
 * @author j-sentinel
 * @date 2024/2/18 15:14
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    ALIPAY_PC("alipay_pc", "支付宝 PC 网站支付", AlipayPayClientConfig.class),

    WALLET("wallet", "钱包支付", NonePayClientConfig.class);

    /**
     * 微信支付
     */
    public static final String WECHAT = "WECHAT";

    /**
     * 支付宝支付
     */
    public static final String ALIPAY = "ALIPAY";

    /**
     * 编码
     *
     * 参考 <a href="https://www.pingxx.com/api/支付渠道属性值.html">支付渠道属性值</a>
     */
    private final String code;

    /**
     * 名字
     */
    private final String name;

    /**
     * 配置类
     */
    private final Class<? extends PayClientConfig> configClass;

    public static PayChannelEnum getByCode(String code) {
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code), values());
    }

}
