package cn.iocoder.educate.framework.sms.core.client.impl.huawei;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.framework.sms.core.client.impl.AbstractSmsClient;
import cn.iocoder.educate.framework.sms.core.client.impl.debug.DebugDingTalkCodeMapping;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;

import java.util.List;

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

    @Override
    protected SmsCommonResult<SmsSendRespDTO> doSendSms(Long sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        return null;
    }

    @Override
    protected void doInit() {

    }
}
