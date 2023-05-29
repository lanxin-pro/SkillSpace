package cn.iocoder.educate.framework.sms.core.client;

import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;

/**
 * 短信客户端的工厂接口
 *
 * @Author: j-sentinel
 * @Date: 2023/5/28 19:27
 */
public interface SmsClientFactory {

    /**
     * 创建短信 Client
     * @param smsChannelProperties1
     */
    void createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties1);
}
