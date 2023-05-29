package cn.iocoder.educate.module.system.service.sms;

import java.util.Map;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/19 11:49
 */
public interface SmsSendService {

    /**
     * 发送单条短信给用户
     * @param mobile 手机号
     * @param userId 用户编号
     * @param userType 用户类型
     * @param templateCode 短信模板编号
     * @param templateParams 短信模板参数
     * @return 发送日志编号
     */
    Long sendSingleSms(String mobile, Long userId, Integer userType,
                       String templateCode, Map<String, Object> templateParams);
}
