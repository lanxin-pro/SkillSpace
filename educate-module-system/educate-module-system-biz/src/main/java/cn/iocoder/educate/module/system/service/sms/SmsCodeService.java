package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;

import javax.validation.Valid;

/**
 * 短信验证码 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:42
 */
public interface SmsCodeService {

    /**
     * 创建短信验证码，并进行发送
     *
     * @param reqDTO 发送请求
     */
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);
}
