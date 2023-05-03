package cn.iocoder.educate.framework.banner.config;

import cn.iocoder.educate.framework.banner.core.BannerApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/3 9:33
 * 理论而言我加不加@Configuration都是一模一样的
 */
@Configuration
public class EducateBannerAutoConfiguration {

    @Bean
    public BannerApplicationRunner bannerApplicationRunner(){
        return new BannerApplicationRunner();
    }
}
