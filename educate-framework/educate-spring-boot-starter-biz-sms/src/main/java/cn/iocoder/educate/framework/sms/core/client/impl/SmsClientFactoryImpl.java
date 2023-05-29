package cn.iocoder.educate.framework.sms.core.client.impl;

import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.client.impl.aliyun.AliyunSmsClient;
import cn.iocoder.educate.framework.sms.core.client.impl.debug.DebugDingTalkSmsClient;
import cn.iocoder.educate.framework.sms.core.client.impl.huawei.HuaweiSmsClient;
import cn.iocoder.educate.framework.sms.core.client.impl.tencent.TencentSmsClient;
import cn.iocoder.educate.framework.sms.core.enums.SmsChannelEnum;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 短信客户端工厂接口
 *
 * @Author: j-sentinel
 * @Date: 2023/5/28 19:27
 */
@Slf4j
@Validated
public class SmsClientFactoryImpl implements SmsClientFactory {

    /**
     *
     */
    private final HashMap<Long, AbstractSmsClient> channelIdClients = new HashMap<>();

    public SmsClientFactoryImpl() {
        // 初始化 channelCodeClients 集合
        Arrays.stream(SmsChannelEnum.values())
                .forEach(channel -> {
                    // 创建一个空的 SmsChannelProperties 对象
                    SmsChannelProperties smsChannelProperties = new SmsChannelProperties().setCode(channel.getCode())
                            .setApiKey("default default").setApiSecret("default");
                    // 创建 Sms 客户端
                    AbstractSmsClient smsClient = createSmsClient(smsChannelProperties);
                });

    }

    @Override
    public void createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties1) {

    }

    private AbstractSmsClient createSmsClient(SmsChannelProperties properties) {
        SmsChannelEnum smsChannelEnum = SmsChannelEnum.getByCode(properties.getCode());
        Assert.notNull(smsChannelEnum,String.format("渠道类型(%s) 为空",smsChannelEnum));
        // 创建客户端
        switch (smsChannelEnum) {
            case ALIYUN: return new AliyunSmsClient(properties);
            case DEBUG_DING_TALK: return new DebugDingTalkSmsClient(properties);
            case TENCENT: return new TencentSmsClient(properties);
            // case HUA_WEI: return new HuaweiSmsClient(properties);
        }
        // 创建失败，错误日志 + 抛出异常
        log.error("[createSmsClient][配置({}) 找不到合适的客户端实现]", properties);
        throw new IllegalArgumentException(String.format("配置(%s) 找不到合适的客户端实现", properties));
    }
}
