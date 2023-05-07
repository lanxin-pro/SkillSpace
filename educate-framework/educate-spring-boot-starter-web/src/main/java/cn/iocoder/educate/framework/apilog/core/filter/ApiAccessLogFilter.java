package cn.iocoder.educate.framework.apilog.core.filter;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLog;
import cn.iocoder.educate.framework.apilog.core.service.ApiAccessLogFrameworkService;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.util.monitor.TracerUtils;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/3 20:23
 * 目前版本有个缺点就是无论什么请求都会进入来执行日志
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
        LocalDateTime beginTime = LocalDateTime.now();
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
                                    LocalDateTime beginTime, Map<String, String> queryString,
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
                                      LocalDateTime beginTime, Map<String, String> queryString, String requestBody,
                                      Exception exception) {
        // 处理用户的信息
        accessLog.setUserId(WebFrameworkUtils.getLoginUserId(request));
        accessLog.setUserType(WebFrameworkUtils.getLoginUserType(request));
        // 设置访问结果
        CommonResult<?> commonResult = WebFrameworkUtils.getCommonResult(request);
        if(commonResult != null){
            accessLog.setResultCode(commonResult.getCode());
            accessLog.setResultMsg(commonResult.getMsg());
        }else if(exception != null){

        }else{
            accessLog.setResultCode(0);
            accessLog.setResultMsg("");
        }
        // 设置其他字段 skywalking
        accessLog.setTraceId(TracerUtils.getTraceId());
        accessLog.setApplicationName(applicationName);
        accessLog.setRequestUrl(request.getRequestURI());
        MapBuilder<String, Object> requestParams = MapUtil.<String, Object>builder().put("query", queryString).put("body", requestBody);
        accessLog.setRequestParams(JSONUtil.toJsonStr(requestParams));
        accessLog.setRequestMethod(request.getMethod());
        accessLog.setUserAgent(ServletUtils.getUserAgent(request));
        accessLog.setUserIp(ServletUtil.getClientIP(request));
        // 持续时间
        accessLog.setBeginTime(beginTime);
        accessLog.setEndTime(LocalDateTime.now());
        accessLog.setDuration((int) LocalDateTimeUtil.between(accessLog.getBeginTime(),accessLog.getEndTime(), ChronoUnit.SECONDS));
    }
}
