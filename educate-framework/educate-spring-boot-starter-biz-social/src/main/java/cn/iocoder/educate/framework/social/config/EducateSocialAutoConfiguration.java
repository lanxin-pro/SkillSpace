package cn.iocoder.educate.framework.social.config;

import cn.iocoder.educate.framework.social.core.EducateAuthRequestFactory;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 社交自动装配类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/7 9:26
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(JustAuthProperties.class)
public class EducateSocialAutoConfiguration {

    /**
     * 仅当配置文件中的 justauth.enabled 属性值为 "true" 或者没有该属性时，才会创建被该注解标记的 Bean
     *
     * @param properties
     * @param authStateCache
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "justauth", value = "enabled", havingValue = "true", matchIfMissing = true)
    public EducateAuthRequestFactory educateAuthRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
        return new EducateAuthRequestFactory(properties, authStateCache);
    }
}
