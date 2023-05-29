package cn.iocoder.educate.framework.sms.core.client;

import cn.iocoder.educate.framework.common.exception.ErrorCode;

import java.util.function.Function;

/**
 * 将 API 的错误码，转换为通用的错误码
 *
 * @Author: 董伟豪
 * @Date: 2023/5/29 20:08
 */
public interface SmsCodeMapping extends Function<String, ErrorCode> {
}
