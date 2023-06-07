package cn.iocoder.educate.framework.common.util.http;

import cn.hutool.core.map.TableMap;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ReflectUtil;

import java.nio.charset.Charset;

/**
 * HTTP 工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/7 11:38
 */
public class HttpUtils {

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
}
