package cn.iocoder.educate.framework.sms.core.client.impl.tencent;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.educate.framework.sms.core.client.impl.AbstractSmsClient;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/29 20:05
 */
public class TencentSmsClient extends AbstractSmsClient {

    /**
     * 调用成功 code
     */
    public static final String API_SUCCESS_CODE = "Ok";

    public TencentSmsClient(SmsChannelProperties properties) {
        super(properties, new TencentSmsCodeMapping());
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }

    @Override
    protected SmsCommonResult<SmsSendRespDTO> doSendSms(Long sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        return null;
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected SmsCommonResult<SmsTemplateRespDTO> doGetSmsTemplate(String apiTemplateId) throws Throwable {
        return null;
    }
}
