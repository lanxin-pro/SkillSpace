package cn.iocoder.educate.module.system.framework.sms;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 不要使用代理对象来调用方法
 *
 * @Author: j-sentinel
 * @Date: 2023/5/20 20:32
 */
@Configuration
@EnableConfigurationProperties(SmsCodeProperties.class)
public class SmsCodeConfiguration {
}
