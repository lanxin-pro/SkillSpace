package cn.iocoder.educate.framework.common.util.servlet;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 9:59
 * 客户端工具类
 */
public class ServletUtils {

    /**
     * 判断是否已指定的字符串开头
     * @param request
     * @return
     */
    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

}
