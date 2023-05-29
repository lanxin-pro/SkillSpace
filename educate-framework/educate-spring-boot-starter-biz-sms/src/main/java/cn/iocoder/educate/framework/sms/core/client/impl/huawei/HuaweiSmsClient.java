package cn.iocoder.educate.framework.sms.core.client.impl.huawei;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.sms.core.client.impl.AbstractSmsClient;
import cn.iocoder.educate.framework.sms.core.client.impl.debug.DebugDingTalkCodeMapping;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/29 20:06
 */
public class HuaweiSmsClient extends AbstractSmsClient {

    public HuaweiSmsClient(SmsChannelProperties properties) {
        super(properties, new DebugDingTalkCodeMapping());
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }
}
