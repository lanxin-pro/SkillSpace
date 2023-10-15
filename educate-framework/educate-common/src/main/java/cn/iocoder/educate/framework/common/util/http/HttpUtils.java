package cn.iocoder.educate.framework.common.util.http;

import cn.hutool.core.map.TableMap;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ReflectUtil;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * HTTP 工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/7 11:38
 */
public class HttpUtils {

    private static final String GOOGLE_REQUEST_PREFIX = "https://accounts.google.com/o/oauth2/v2/auth";

    /**
     * 将指定 URL 中的查询参数 key 的值替换为指定的 value 值
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String replaceUrlQuery(String url, String key, String value) {
        // 裁剪Google的重定向
        value = replaceUrlGoogleVerificationURL(url,value);
        // 使用 UrlBuilder 类将传入的 URL 解析为一个可编辑的 URL 对象；
        UrlBuilder builder = UrlBuilder.of(url, Charset.defaultCharset());
        // 通过 Java 反射的方式获取该 URL 对象中的查询参数，并将其中原有的 key 对应的值移除；
        TableMap<CharSequence, CharSequence> query = (TableMap<CharSequence, CharSequence>)
                ReflectUtil.getFieldValue(builder.getQuery(), "query");
        query.remove(key);
        // 使用 builder.addQuery(key, value) 方法添加新的查询参数；
        builder.addQuery(key, value);
        return builder.build();
    }

    /**
     * 裁剪Google的重定向
     *
     * @param url 完整google的url
     * @param value 重定向的url
     * @return
     */
    private static String replaceUrlGoogleVerificationURL(String url, String value) {
        if(url.startsWith(GOOGLE_REQUEST_PREFIX)){
            // 裁剪掉 &redirect=/ 部分
            value = value.replaceAll("&redirect=.*", "");
        }
        return value;
    }

    /**
     * 拼接 URL
     *
     * copy from Spring Security OAuth2 的 AuthorizationEndpoint 类的 append 方法
     *
     * @param base 基础 URL
     * @param query 查询参数
     * @param keys query 的 key，对应的原本的 key 的映射。例如说 query 里有个 key 是 xx，实际它的 key 是 extra_xx，则通过 keys 里添加这个映射
     * @param fragment URL 的 fragment，即拼接到 # 中
     * @return 拼接后的 URL
     */
    public static String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
        URI redirectUri;
        try {
            // assume it's encoded to start with (if it came in over the wire)
            redirectUri = builder.build(true).toUri();
        } catch (Exception e) {
            // ... but allow client registrations to contain hard-coded non-encoded values
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                String append = redirectUri.getFragment();
                values.append(append);
            }
            for (String key : query.keySet()) {
                if (values.length() > 0) {
                    values.append("&");
                }
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                values.append(name).append("={").append(key).append("}");
            }
            if (values.length() > 0) {
                template.fragment(values.toString());
            }
            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for (String key : query.keySet()) {
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                template.queryParam(name, "{" + key + "}");
            }
            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }
        return builder.build().toUriString();
    }

}
