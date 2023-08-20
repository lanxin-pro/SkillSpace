package cn.iocoder.educate.framework.errorcode.config;

import cn.iocoder.educate.framework.errorcode.core.generator.ErrorCodeAutoGenerator;
import cn.iocoder.educate.framework.errorcode.core.generator.ErrorCodeAutoGeneratorImpl;
import cn.iocoder.educate.module.system.api.errorcode.ErrorCodeApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/20 11:07
 */
@Slf4j
@Configuration
// 允许使用 educate.error-code.enable=false 禁用访问日志
@ConditionalOnProperty(prefix = "educate.error-code", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(ErrorCodeProperties.class)
// 开启调度任务的功能，因为 ErrorCodeRemoteLoader 通过定时刷新错误码
@EnableScheduling
public class EducateErrorCodeConfiguration {

    /**
     * 在数据中生成错误码
     * @param applicationName 应用名
     * @param errorCodeProperties 需要读取的配置类
     * @param errorCodeApi 接口
     * @return
     */
    @Bean
    public ErrorCodeAutoGenerator errorCodeAutoGenerator(
            @Value("${spring.application.name}") String applicationName,
            ErrorCodeProperties errorCodeProperties,
            ErrorCodeApi errorCodeApi) {
        return new ErrorCodeAutoGeneratorImpl(
                applicationName,
                errorCodeProperties.getConstantsClassList(),
                errorCodeApi);
    }

}
