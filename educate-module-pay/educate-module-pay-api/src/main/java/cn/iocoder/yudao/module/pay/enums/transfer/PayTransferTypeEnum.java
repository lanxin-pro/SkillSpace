package cn.iocoder.yudao.module.pay.enums.transfer;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 转账类型枚举
 *
 * @author j-sentinel
 * @date 2024/4/5 15:05
 */
@AllArgsConstructor
@Getter
public enum PayTransferTypeEnum implements IntArrayValuable {

    ALIPAY_BALANCE(1, "支付宝余额"),
    WX_BALANCE(2, "微信余额"),
    BANK_CARD(3, "银行卡"),
    WALLET_BALANCE(4, "钱包余额");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayTransferTypeEnum::getType).toArray();
    private final Integer type;
    private final String name;

    public static PayTransferTypeEnum typeOf(Integer type) {
        return ArrayUtil.firstMatch(item -> item.getType().equals(type), values());
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public interface WxPay {
    }

    public interface Alipay {
    }

}
