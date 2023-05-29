package cn.iocoder.educate.framework.sms.core.client.impl.debug;

import cn.iocoder.educate.framework.common.exception.ErrorCode;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.educate.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.educate.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;
import java.util.Objects;

/**
 * 钉钉的 SmsCodeMapping 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/29 21:00
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0")
                ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;

    }
}
