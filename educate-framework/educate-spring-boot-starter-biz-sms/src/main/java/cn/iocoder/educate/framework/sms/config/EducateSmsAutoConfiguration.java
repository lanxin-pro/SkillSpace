package cn.iocoder.educate.framework.sms.config;

import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/28 19:26
 */
@Configuration
public class EducateSmsAutoConfiguration {

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }

}
