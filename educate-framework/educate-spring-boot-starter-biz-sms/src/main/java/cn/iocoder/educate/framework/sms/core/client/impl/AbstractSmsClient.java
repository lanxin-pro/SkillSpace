package cn.iocoder.educate.framework.sms.core.client.impl;

import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.sms.core.client.SmsClient;
import cn.iocoder.educate.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    @Override
    public SmsCommonResult<SmsSendRespDTO> sendSms(Long logId, String mobile, String apiTemplateId,
                                                   List<KeyValue<String, Object>> templateParams) {
        // 执行短信发送
        SmsCommonResult<SmsSendRespDTO> result;
        try {
            result = doSendSms(logId, mobile, apiTemplateId, templateParams);
        } catch (Throwable ex) {
            // 打印异常日志
            log.error("[sendSms][发送短信异常，sendLogId({}) mobile({}) apiTemplateId({}) templateParams({})]",
                    logId, mobile, apiTemplateId, templateParams, ex);
            // 封装返回
            return SmsCommonResult.error(ex);
        }
        return result;
    }

    protected abstract SmsCommonResult<SmsSendRespDTO> doSendSms(Long sendLogId, String mobile,
                                                                 String apiTemplateId, List<KeyValue<String, Object>> templateParams)
            throws Throwable;

    /**
     * 自定义初始化
     * 从文档从看出，我在调用短信发送之前就得初始化这个方法
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
