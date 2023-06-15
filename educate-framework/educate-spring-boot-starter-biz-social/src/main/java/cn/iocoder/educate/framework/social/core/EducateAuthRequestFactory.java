package cn.iocoder.educate.framework.social.core;

import com.xkcoding.justauth.AuthRequestFactory;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthRequest;

/**
 * 第三方授权拓展 request 工厂类
 *
 * 为使得拓展配置 {@link AuthConfig} 和默认配置齐平，所以自定义本工厂类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/7 11:15
 */
@Slf4j
public class EducateAuthRequestFactory extends AuthRequestFactory {

    protected JustAuthProperties properties;

    protected AuthStateCache authStateCache;

    public EducateAuthRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
        super(properties, authStateCache);
        this.properties = properties;
        this.authStateCache = authStateCache;
    }

    /**
     * 返回 AuthRequest 对象
     *
     * @param source
     * @return
     */
    @Override
    public AuthRequest get(String source) {
        return super.get(source);
    }
}
