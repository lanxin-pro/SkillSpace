package cn.iocoder.educate.ssodemo.framework.config;

import cn.iocoder.educate.ssodemo.framework.core.handler.AccessDeniedHandlerImpl;
import cn.iocoder.educate.ssodemo.framework.core.handler.AuthenticationEntryPointImpl;
import cn.iocoder.educate.ssodemo.framework.filter.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/15 13:17
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 设置 URL 安全权限
        httpSecurity.csrf().disable() // 禁用 CSRF 保护
                .authorizeRequests()
                // 1. 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                // 2. 登录相关的接口，可匿名访问
                .antMatchers("/auth/login-by-code").permitAll()
                .antMatchers("/auth/refresh-token").permitAll()
                .antMatchers("/auth/logout").permitAll()
                // last. 兜底规则，必须认证
                .and().authorizeRequests()
                .anyRequest().authenticated();

        // 设置处理器
        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        // 添加 Token Filter
        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
