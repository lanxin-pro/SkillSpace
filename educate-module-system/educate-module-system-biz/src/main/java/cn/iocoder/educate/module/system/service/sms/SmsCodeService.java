package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeUseReqDTO;

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

    /**
     * 验证短信验证码，并进行使用
     * 如果正确，则将验证码标记成已使用
     * 如果错误，则抛出 {@link ServiceException} 异常
     *
     * @param smsCodeUseReqDTO 使用请求
     */
    void useSmsCode(SmsCodeUseReqDTO smsCodeUseReqDTO);
}
