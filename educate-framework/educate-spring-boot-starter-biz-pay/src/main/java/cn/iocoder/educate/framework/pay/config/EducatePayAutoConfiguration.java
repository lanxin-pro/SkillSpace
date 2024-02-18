package cn.iocoder.educate.framework.pay.config;

import cn.iocoder.educate.framework.pay.core.client.PayClientFactory;
import cn.iocoder.educate.framework.pay.core.client.impl.PayClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author j-sentinel
 * @date 2024/2/18 14:26
 */
@Configuration
public class EducatePayAutoConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
