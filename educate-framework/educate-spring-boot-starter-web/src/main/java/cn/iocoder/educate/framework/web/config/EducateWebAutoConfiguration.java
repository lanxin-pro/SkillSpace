package cn.iocoder.educate.framework.web.config;

import cn.hutool.core.text.AntPathMatcher;
import cn.iocoder.educate.framework.apilog.core.service.ApiErrorLogFrameworkService;
import cn.iocoder.educate.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.educate.framework.web.core.handler.GlobalExceptionHandler;
import cn.iocoder.educate.framework.web.core.handler.GlobalResponseBodyHandler;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;
import javax.servlet.Filter;

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
     * 我就admin/app下面添加标识
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
    public GlobalExceptionHandler globalExceptionHandler(ApiErrorLogFrameworkService apiErrorLogFrameworkService) {
        return new GlobalExceptionHandler(applicationName, apiErrorLogFrameworkService);
    }

    @Bean
    public GlobalResponseBodyHandler globalResponseBodyHandler() {
        return new GlobalResponseBodyHandler();
    }

    /**
     * 由于 WebFrameworkUtils 需要使用到 webProperties 属性，所以注册为一个 Bean
     *
     * @param webProperties
     * @return
     */
    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public WebFrameworkUtils webFrameworkUtils(WebProperties webProperties){
        return new WebFrameworkUtils(webProperties);
    }

    // ========== Filter 相关 ==========

    /**
     * 创建 CorsFilter Bean，解决跨域问题
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // 创建 CorsConfiguration 对象
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 设置访问源地址
        config.addAllowedHeader("*"); // 设置访问源请求头
        config.addAllowedMethod("*"); // 设置访问源请求方法
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
        return createFilterBean(new CorsFilter(source), WebFilterOrderEnum.CORS_FILTER);
    }

    public static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

}
