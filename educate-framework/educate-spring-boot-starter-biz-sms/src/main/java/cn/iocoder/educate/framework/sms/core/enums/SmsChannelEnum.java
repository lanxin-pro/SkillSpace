package cn.iocoder.educate.framework.sms.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信渠道枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/5/29 19:46
 */
@Getter
@AllArgsConstructor
public enum SmsChannelEnum {

    ALIYUN("ALIYUN", "阿里云"),
    DEBUG_DING_TALK("DEBUG_DING_TALK", "调试(钉钉)"),
    TENCENT("TENCENT", "腾讯云"),
    // HUA_WEI("HUA_WEI", "华为云"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 名字
     */
    private final String name;

    public static SmsChannelEnum getByCode(String code){
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code),values());
    }
}
