package cn.iocoder.educate.framework.captcha.config;

import cn.iocoder.educate.framework.captcha.core.service.RedisCaptchaServiceImpl;
import com.xingyuv.captcha.service.CaptchaCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 17:18
 */
public class EducateCaptchaConfiguration {

    @Bean
    public CaptchaCacheService captchaCacheService(StringRedisTemplate stringRedisTemplate) {
        return new RedisCaptchaServiceImpl(stringRedisTemplate);
    }
}
