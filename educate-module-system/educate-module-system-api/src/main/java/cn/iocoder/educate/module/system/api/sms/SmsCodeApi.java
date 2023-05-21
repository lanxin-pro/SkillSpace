package cn.iocoder.educate.module.system.api.sms;

import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
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
}
