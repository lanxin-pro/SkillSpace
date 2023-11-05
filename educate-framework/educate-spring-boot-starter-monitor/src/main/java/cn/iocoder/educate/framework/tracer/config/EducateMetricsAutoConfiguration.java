package cn.iocoder.educate.framework.tracer.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Metrics 配置类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/5 15:46
 */
@Configuration
@ConditionalOnClass({MeterRegistryCustomizer.class})
// 允许使用 yudao.metrics.enable=false 禁用 Metrics
@ConditionalOnProperty(prefix = "educate.metrics", value = "enable", matchIfMissing = true)
public class EducateMetricsAutoConfiguration {
}
