package cn.iocoder.educate.framework.sms.core.client.impl.aliyun;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.educate.framework.sms.core.client.impl.AbstractSmsClient;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import com.aliyuncs.IAcsClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里短信客户端的实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/29 19:57
 */
@Slf4j
public class AliyunSmsClient extends AbstractSmsClient {

    /**
     * REGION, 使用杭州
     */
    private static final String ENDPOINT = "cn-hangzhou";

    /**
     * 阿里云客户端
     */
    private volatile IAcsClient client;

    public AliyunSmsClient(SmsChannelProperties smsChannelProperties) {
        super(smsChannelProperties, new AliyunSmsCodeMapping());
        Assert.notEmpty(smsChannelProperties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(smsChannelProperties.getApiSecret(), "apiSecret 不能为空");
    }
}
