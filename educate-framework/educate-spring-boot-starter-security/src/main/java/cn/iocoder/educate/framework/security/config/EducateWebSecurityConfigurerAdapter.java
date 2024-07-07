package cn.iocoder.educate.framework.security.config;

import cn.iocoder.educate.framework.security.core.filter.TokenAuthenticationFilter;
import cn.iocoder.educate.framework.security.core.handler.AccessDeniedHandlerImpl;
import cn.iocoder.educate.framework.security.core.handler.AuthenticationEntryPointImpl;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义的 Spring Security 配置适配器实现
 *
 * 启用 @PreAuthorize 和 @PostAuthorize 注解
 * 启用 @Secured 注解
 * @Author: j-sentinel
 * @Date: 2023/5/13 14:23
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class EducateWebSecurityConfigurerAdapter {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private TokenAuthenticationFilter authenticationTokenFilter;

    /**
     * 配置 URL 的安全配置
     * <p>
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 开启跨域访问
                .cors(Customizer.withDefaults())
                // CSRF 禁用，因为不使用 Session
                .csrf(AbstractHttpConfigurer::disable)
                // 基于 token 机制，所以不需要 Session
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 防止将报头添加到响应中
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 一堆自定义的 Spring Security 处理器                认证失败处理类
                .exceptionHandling(c -> c.authenticationEntryPoint(new AuthenticationEntryPointImpl())
                        // 权限不够处理器
                        .accessDeniedHandler(new AccessDeniedHandlerImpl()));
        // 登录暂时不使用 Spring Security 的拓展点，主要考虑一方面拓展多用户、多种登录方式相对复杂，一方面用户的学习成本较高


        // 获得 @PermitAll 带来的 URL 列表，免登录
        Multimap<HttpMethod, String> permitAllUrls = getPermitAllUrlsFromAnnotations();

        httpSecurity
                // 1.全局共享的规则
                .authorizeHttpRequests(c -> c
                        // 1.1 静态资源，可匿名访问
                        .requestMatchers(HttpMethod.GET,"/*.html","/*.css","/*.js").permitAll()
                        // 1.2 设置 @PermitAll 无需认证  最后转换成字符串数组
                        .requestMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
                        // 1.5 验证码captcha 允许匿名访问
                        .requestMatchers("/captcha/get", "/captcha/check").permitAll()
                        // 1.6 webSocket 允许匿名访问 @PreAuthenticated是声明App用户不用登录的接口
                        .requestMatchers("/websocket/message").permitAll()
                        // TODO j-sentinel swagger文档json格式的测试
                        // Swagger 接口文档
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-resources/**").anonymous()
                        .requestMatchers("/webjars/**").anonymous()
                        .requestMatchers("/*/api-docs").anonymous()
                        // Spring Boot Actuator 的安全配置
                        .requestMatchers("/actuator").anonymous()
                        .requestMatchers("/actuator/**").anonymous()
                        // Spring Boot Admin Server 的安全配置
                        .requestMatchers("/admin").anonymous()
                        .requestMatchers("/admin/**").anonymous()
                        // Druid 监控
                        .requestMatchers("/druid/**").anonymous()
                        .anyRequest().authenticated()
                );


        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        // 如果不使用HashMultimap我就必须使用这样非常麻烦的方式 Map<Integer,List<Object>>，当key为同一个的时候,value可以追加成为一个去重后的List
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // RequestMappingHandlerMapping是springMVC提供的获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        // 所有springController的接口
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class)) {
                continue;
            }
            // 这里判断的是requestMapping路径是否为空
            if (entry.getKey().getPathPatternsCondition() == null) {
                continue;
            }
            // 返回接口方法
            // Set<String> urls = entry.getKey().getPatternsCondition().getPatterns();

            Set<String> urls = entry.getKey().getPathPatternsCondition().getPatterns()
                    .stream().map(PathPattern::getPatternString)
                    .collect(Collectors.toSet());
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                }
            });
        }

        return result;
    }

}
