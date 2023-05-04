package cn.iocoder.educate.framework.apilog.core.filter;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLog;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLogFrameworkService;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/3 20:23
 */
@Slf4j
public class ApiAccessLogFilter extends OncePerRequestFilter {

    private final String applicationName;

    private final ApiAccessLogFrameworkService apiAccessLogFrameworkService;

    public ApiAccessLogFilter(String applicationName, ApiAccessLogFrameworkService apiAccessLogFrameworkService) {
        this.applicationName = applicationName;
        this.apiAccessLogFrameworkService = apiAccessLogFrameworkService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取开始时间
        LocalTime beginTime = LocalTime.now();
        // 提前获得参数，避免 XssFilter 过滤处理
        Map<String, String> queryString = ServletUtil.getParamMap(request);
        String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtil.getBody(request) : null;

        try {
            filterChain.doFilter(request, response);
            // 正常执行，记录日志
            createApiAccessLog(request, response,beginTime, queryString, requestBody, null);
        }catch (Exception exception){
            // 异常执行，记录日志
            createApiAccessLog(request, response,beginTime, queryString, requestBody, exception);
            throw exception;
        }
    }

    private void createApiAccessLog(HttpServletRequest request, HttpServletResponse response,
                                    LocalTime beginTime, Map<String, String> queryString,
                                    String requestBody, Exception exception) {
        ApiAccessLog accessLog = new ApiAccessLog();
        try{
            buildApiAccessLogDTO(accessLog,request,response,beginTime,queryString,requestBody,exception);
            apiAccessLogFrameworkService.createApiAccessLog(accessLog);
        }catch (Throwable throwable){
            log.error("[createApiAccessLog][url({}) log({}) 发生异常]",request.getRequestURI(), JSONUtil.toJsonStr(accessLog),throwable);
        }
    }

    /**
     * 对AccessLog日志的构建
     * @param accessLog
     * @param request
     * @param response
     * @param beginTime
     * @param queryString
     * @param requestBody
     * @param exception
     */
    private void buildApiAccessLogDTO(ApiAccessLog accessLog, HttpServletRequest request, HttpServletResponse response,
                                      LocalTime beginTime, Map<String, String> queryString, String requestBody,
                                      Exception exception) {

    }
}
