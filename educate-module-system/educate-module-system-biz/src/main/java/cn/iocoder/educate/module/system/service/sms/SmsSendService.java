package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.mq.message.sms.SmsSendMessage;

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

    /**
     * 执行真正的短信发送
     * 注意，该方法仅仅提供给 MQ Consumer 使用
     *
     * @param message
     */
    void doSendSms(SmsSendMessage message);

    /**
     * 发送单条短信给管理后台的用户
     *
     * 在 mobile 为空时，使用 userId 加载对应管理员的手机号
     *
     * @param mobile 手机号
     * @param userId 用户编号
     * @param templateCode 短信模板编号
     * @param templateParams 短信模板参数
     * @return 发送日志编号
     */
    Long sendSingleSmsToAdmin(String mobile, Long userId,
                              String templateCode, Map<String, Object> templateParams);

}
