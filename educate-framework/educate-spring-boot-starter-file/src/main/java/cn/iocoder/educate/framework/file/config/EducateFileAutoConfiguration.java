package cn.iocoder.educate.framework.file.config;

import cn.iocoder.educate.framework.file.core.client.FileClientFactory;
import cn.iocoder.educate.framework.file.core.client.FileClientFactoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:41
 */
@Slf4j
@Configuration
public class EducateFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory(){
        return new FileClientFactoryImpl();
    }

}
