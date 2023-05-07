package cn.iocoder.educate.framework.operatelog.config;

import cn.iocoder.educate.framework.operatelog.core.aop.OperateLogAspect;
import cn.iocoder.educate.framework.operatelog.core.service.OperateLogFrameworkService;
import cn.iocoder.educate.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import cn.iocoder.educate.module.system.api.logger.OperateLogApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 10:54
 */
@Configuration
public class EducateOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect(){
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi){
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }
}
