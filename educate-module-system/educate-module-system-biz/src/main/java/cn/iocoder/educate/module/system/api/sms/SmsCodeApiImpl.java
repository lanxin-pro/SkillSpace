package cn.iocoder.educate.module.system.api.sms;

import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.educate.module.system.service.sms.SmsCodeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * 短信验证码 API 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:38
 */
@Service
@Validated
public class SmsCodeApiImpl implements SmsCodeApi {

    @Resource
    private SmsCodeService smsCodeService;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO smsCodeSendReqDTO) {
        smsCodeService.sendSmsCode(smsCodeSendReqDTO);
    }

    @Override
    public void useSmsCode(SmsCodeUseReqDTO smsCodeUseReqDTO) {
        smsCodeService.useSmsCode(smsCodeUseReqDTO);
    }
}
