package cn.iocoder.educate.framework.sms.core.client.impl;

import cn.iocoder.educate.framework.sms.core.client.SmsClient;
import cn.iocoder.educate.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/29 19:44
 */
@Slf4j
public abstract class AbstractSmsClient implements SmsClient {

    /**
     * 短信渠道配置
     */
    protected volatile SmsChannelProperties properties;

    /**
     * 错误码枚举类
     */
    protected final SmsCodeMapping codeMapping;

    protected AbstractSmsClient(SmsChannelProperties smsChannelProperties,SmsCodeMapping codeMapping) {
        // 自定义规则
        this.properties = prepareProperties(smsChannelProperties);
        this.codeMapping = codeMapping;
    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.info("[init][配置({}) 初始化完成]", properties);
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    /**
     * 在赋值给{@link this#properties}前，子类可根据需要预处理短信渠道配置
     *
     * @param properties 数据库中存储的短信渠道配置
     * @return 满足子类实现的短信渠道配置
     */
    protected SmsChannelProperties prepareProperties(SmsChannelProperties properties) {
        return properties;
    }

    public Long getId() {
        return properties.getId();
    }
}
