package cn.iocoder.educate.module.mp.framework.mp.config;

import cn.iocoder.educate.module.mp.framework.mp.core.DefaultMpServiceFactory;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.service.handler.message.MessageAutoReplyHandler;
import cn.iocoder.educate.module.mp.service.handler.message.MessageReceiveHandler;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/31 15:34
 */
@Configuration
public class MpConfiguration {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps(StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateWxRedisOps(stringRedisTemplate);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MpServiceFactory mpServiceFactory(RedisTemplateWxRedisOps redisTemplateWxRedisOps,
                                             WxMpProperties wxMpProperties,
                                             MessageAutoReplyHandler messageAutoReplyHandler,
                                             MessageReceiveHandler messageReceiveHandler){
        return new DefaultMpServiceFactory(redisTemplateWxRedisOps,wxMpProperties,
                messageAutoReplyHandler,messageReceiveHandler);
    }

}
