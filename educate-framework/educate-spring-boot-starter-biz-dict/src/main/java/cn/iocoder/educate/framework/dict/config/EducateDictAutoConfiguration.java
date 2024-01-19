package cn.iocoder.educate.framework.dict.config;

import cn.iocoder.educate.framework.dict.core.util.DictFrameworkUtils;
import cn.iocoder.educate.module.system.api.dict.DictDataApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2024/1/18 15:17
 */
@Configuration
public class EducateDictAutoConfiguration {

    @Bean
    public DictFrameworkUtils dictUtils(DictDataApi dictDataApi) {
        DictFrameworkUtils.init(dictDataApi);
        return new DictFrameworkUtils();
    }

}
