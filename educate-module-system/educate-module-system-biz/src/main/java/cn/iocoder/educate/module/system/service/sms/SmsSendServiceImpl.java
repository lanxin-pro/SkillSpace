package cn.iocoder.educate.module.system.service.sms;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信发送 Service 发送的实现
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:50
 */
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        return null;
    }
}
