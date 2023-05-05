package cn.iocoder.educate.framework.apilog.config;

import cn.iocoder.educate.framework.apilog.core.filter.ApiAccessLogFilter;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLogFrameworkService;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLogFrameworkServiceImpl;
import cn.iocoder.educate.module.infra.api.logger.ApiAccessLogApi;
import cn.iocoder.educate.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/5 8:39
 */
@Configuration
public class EducateApiLogAutoConfiguration {

    /**
     * ApiAccessLogApi避免apiAccessLogApi没有注册bean
     * 对代码更加优雅的处理
     * @return
     */
    @Bean
    public ApiAccessLogApi accessLog(){
        return (apiAccessLogCreateReqDTO) -> {};
    }

    /**
     * 加上这个可以直接给#{apiAccessLogFilter}的参数注入bean了
     * @param apiAccessLogApi
     * @return
     */
    @Bean
    public ApiAccessLogFrameworkService apiAccessLogFrameworkService(ApiAccessLogApi apiAccessLogApi) {
        return new ApiAccessLogFrameworkServiceImpl(apiAccessLogApi);
    }


    /**
     * 创建 ApiAccessLogFilter Bean，记录 API 请求日志
     * 允许使用 educate.access-log.enable=false 禁用访问日志
     * @param applicationName
     * @param apiAccessLogFrameworkService
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "educate.access-log",value = "enable",matchIfMissing = true)
    public ApiAccessLogFilter apiAccessLogFilter(@Value("${spring.application.name}") String applicationName,
                                                 ApiAccessLogFrameworkService apiAccessLogFrameworkService){
        return new ApiAccessLogFilter(applicationName,apiAccessLogFrameworkService);
    }

}
