package cn.iocoder.educate.framework.ai.config;

import cn.iocoder.educate.framework.ai.core.factory.AiClientFactory;
import cn.iocoder.educate.framework.ai.core.factory.AiClientFactoryImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author j-sentinel
 * @date 2024/7/2 10:31
 */
@EnableConfigurationProperties(EducateAiProperties.class)
public class EducateAiAutoConfiguration {

    @Bean
    public AiClientFactory aiClientFactory() {
        return new AiClientFactoryImpl();
    }

}
