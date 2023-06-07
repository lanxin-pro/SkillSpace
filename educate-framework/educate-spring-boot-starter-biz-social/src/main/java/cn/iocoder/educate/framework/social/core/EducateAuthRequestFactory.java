package cn.iocoder.educate.framework.social.core;

import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/7 11:15
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class EducateAuthRequestFactory {

    protected JustAuthProperties properties;

    protected AuthStateCache authStateCache;

}
