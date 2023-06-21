package cn.iocoder.educate.module.system.api.sms;

import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeUseReqDTO;

import javax.validation.Valid;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/19 11:38
 */
public interface SmsCodeApi {

    /**
     * 创建短信验证码，并进行发送
     * @param smsCodeSendReqDTO
     */
    void sendSmsCode(@Valid SmsCodeSendReqDTO smsCodeSendReqDTO);

    /**
     * 验证短信验证码，并进行使用
     * 如果正确，则将验证码标记成已使用
     * 如果错误，则抛出 {@link ServiceException} 异常
     *
     * @param smsCodeUseReqDTO 使用请求
     */
    void useSmsCode(SmsCodeUseReqDTO smsCodeUseReqDTO);
}
