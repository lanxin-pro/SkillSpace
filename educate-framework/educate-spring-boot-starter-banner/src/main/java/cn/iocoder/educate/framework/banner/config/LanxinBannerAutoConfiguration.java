package cn.iocoder.educate.framework.banner.config;

import cn.iocoder.educate.framework.banner.core.BannerApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/3 9:33
 */
@Configuration
public class LanxinBannerAutoConfiguration {

    @Bean
    public BannerApplicationRunner bannerApplicationRunner(){
        return new BannerApplicationRunner();
    }
}
