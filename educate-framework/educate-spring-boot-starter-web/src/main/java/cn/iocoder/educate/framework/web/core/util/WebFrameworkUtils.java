package cn.iocoder.educate.framework.web.core.util;

import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.web.config.WebProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/5 8:08
 * 专属于 web 包的工具类
 */
public class WebFrameworkUtils {

    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";

    private static final String REQUEST_ATTRIBUTE_COMMON_RESULT = "common_result";

    public static final String HEADER_TENANT_ID = "tenant-id";

    private static WebProperties properties;

    /**
     * 初始化？？？
     * 如果直接写的话，确实无法触发初始化，但是我把这个注入bean后就可以了
     * @param webProperties
     */
    public WebFrameworkUtils(WebProperties webProperties) {
        WebFrameworkUtils.properties = webProperties;
    }

    /**
     * 获得当前用户的编号，从请求中
     * 注意：该方法仅限于 framework 框架使用！！！
     *
     * @param request 请求
     * @return 用户编号
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    /**
     * 获取用户的id
     * @return
     */
    public static Long getLoginUserId() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    /**
     * 设置用户的id
     *
     * @param request
     * @param userId
     */
    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    /**
     * 设置用户类型
     *
     * @param request 请求
     * @param userType 用户类型
     */
    public static void setLoginUserType(ServletRequest request, Integer userType) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE, userType);
    }

    /**
     * 获取用户的类型
     * @return
     */
    public static Integer getLoginUserType(){
        HttpServletRequest request = getRequest();
        return getLoginUserType(request);
    }

    /**
     * 获得当前用户的类型
     * 注意：该方法仅限于 web 相关的 framework 组件使用！！！
     *
     * @param request 请求
     * @return 用户编号
     */
    public static Integer getLoginUserType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 1. 优先，从 Attribute 中获取
        Integer userType = (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
        if (userType != null) {
            return userType;
        }
        // 2. 其次，基于 URL 前缀的约定，就是用户APP和后端管理页面的admin来区分的
        String prefix = properties.getAdminApi().getPrefix();
        prefix = "/" + properties.getPrePath() + prefix;
        if (request.getRequestURI().startsWith(prefix)) {
            return UserTypeEnum.ADMIN.getValue();
        }
        if (request.getRequestURI().startsWith(properties.getAppApi().getPrefix())) {
            return UserTypeEnum.MEMBER.getValue();
        }
        return null;
    }

    public static CommonResult<?> getCommonResult(ServletRequest request) {
        return (CommonResult<?>) request.getAttribute(REQUEST_ATTRIBUTE_COMMON_RESULT);
    }

    /**
     * 获取当前线程处理的 HTTP 请求相关的信息
     * 通过 Spring 框架中的 RequestContextHolder 类和 ServletRequestAttributes 类实现的
     * 1.RequestContextHolder.getRequestAttributes() 方法获取当前线程对应的 RequestAttributes 实例
     *   该实例在当前线程执行请求时填充。
     * 2.判断 RequestAttributes 是否实现了 ServletRequestAttributes 类型，如果不是，返回 null。
     * 3.强制将 RequestAttributes 转换成 ServletRequestAttributes 类型，通过调用 getRequest() 方法获
     *   取 HttpServletRequest 对象，即当前请求的上下文对象，从而获取 HTTP 请求中的信息，如请求头、请求参数等。
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

    public static void setCommonResult(HttpServletRequest request, Object result) {
        request.setAttribute(REQUEST_ATTRIBUTE_COMMON_RESULT, result);
    }
}
