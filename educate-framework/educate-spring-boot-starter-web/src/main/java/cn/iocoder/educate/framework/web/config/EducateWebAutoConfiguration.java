package cn.iocoder.educate.framework.web.config;

import cn.hutool.core.text.AntPathMatcher;
import cn.iocoder.educate.framework.apilog.core.service.ApiErrorLogFrameworkService;
import cn.iocoder.educate.framework.web.core.handler.GlobalExceptionHandler;
import cn.iocoder.educate.framework.web.core.handler.GlobalResponseBodyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 16:21
 */
@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class EducateWebAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private WebProperties webProperties;

    /**
     * 应用名
     */
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurePathMatch(configurer,webProperties.getAdminApi());
        configurePathMatch(configurer,webProperties.getAppApi());
    }

    /**
     * 设置 API 前缀，仅仅匹配 controller 包下的
     * @param configurer 配置
     * @param api        API 配置
     */
    public void configurePathMatch(PathMatchConfigurer configurer,WebProperties.Api api) {
        // /
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 如果不存在,则排除该
        configurer.addPathPrefix(api.getPrefix(),
                clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match(api.getController(),clazz.getPackage().getName()) // 仅仅匹配 controller 包
        );
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ApiErrorLogFrameworkService ApiErrorLogFrameworkService) {
        return new GlobalExceptionHandler(applicationName, ApiErrorLogFrameworkService);
    }

    @Bean
    public GlobalResponseBodyHandler globalResponseBodyHandler() {
        return new GlobalResponseBodyHandler();
    }
}
