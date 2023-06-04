package cn.iocoder.educate.framework.sms.core.client.impl;

import cn.iocoder.educate.framework.sms.core.client.SmsClient;
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
     * 短信客户端 Map
     *
     * 首轮加载这个肯定是空的，需要赋值
     *
     *  key：渠道编号，使用 {@link SmsChannelProperties#getId()}
     */
    private final HashMap<Long, AbstractSmsClient> channelIdClients = new HashMap<>();

    /**
     * 短信客户端 Map
     *
     * 首轮加载的时候，会把 {@link SmsChannelEnum} 枚举全部加载
     *
     * key：渠道编码，使用 {@link SmsChannelProperties#getCode()} ()}
     *
     * 注意，一些场景下，需要获得某个渠道类型的客户端，所以需要使用它。
     * 例如说，解析短信接收结果，是相对通用的，不需要使用某个渠道编号的 {@link #channelIdClients}
     */
    private final HashMap<String, AbstractSmsClient> channelCodeClients = new HashMap<>();

    public SmsClientFactoryImpl() {
        // 初始化 channelCodeClients 集合
        Arrays.stream(SmsChannelEnum.values())
                .forEach(channel -> {
                    // 创建一个空的 SmsChannelProperties 对象
                    SmsChannelProperties smsChannelProperties = new SmsChannelProperties().setCode(channel.getCode())
                            // 首轮加载的 apiKey 肯定是没有的
                            .setApiKey("default default").setApiSecret("default");
                    // 创建 Sms 客户端
                    AbstractSmsClient smsClient = createSmsClient(smsChannelProperties);
                    channelCodeClients.put(channel.getCode(), smsClient);
                });

    }

    @Override
    public void createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties1) {
        AbstractSmsClient abstractSmsClient = channelIdClients.get(smsChannelProperties1.getId());
        if(abstractSmsClient == null){
            abstractSmsClient = createSmsClient(smsChannelProperties1);
            abstractSmsClient.init();
            channelIdClients.put(abstractSmsClient.getId(),abstractSmsClient);
        }
    }

    /**
     * 根据channelId获取客户端
     * @param channelId 渠道编号
     * @return
     */
    @Override
    public SmsClient getSmsClient(Long channelId) {
        return channelIdClients.get(channelId);
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
