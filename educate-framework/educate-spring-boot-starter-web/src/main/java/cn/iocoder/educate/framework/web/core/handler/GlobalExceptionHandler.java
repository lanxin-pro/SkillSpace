package cn.iocoder.educate.framework.web.core.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.apilog.core.service.ApiErrorLog;
import cn.iocoder.educate.framework.apilog.core.service.ApiErrorLogFrameworkService;
import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.util.monitor.TracerUtils;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Map;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_FOUND;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.METHOD_NOT_ALLOWED;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.TOO_MANY_REQUESTS;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.FORBIDDEN;
import static cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR;


/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 16:59
 *
 * 全局异常处理器，将 Exception 翻译成 CommonResult + 对应的异常编号
 */
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    private final String applicationName;

    private final ApiErrorLogFrameworkService apiErrorLogFrameworkService;

    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        // 断言，避免告警 如何错误就直接抛出AssertionError
        assert fieldError != null;
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        // 断言，避免告警
        assert fieldError != null;
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 Dubbo Consumer 本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler(value = ValidationException.class)
    public CommonResult<?> validationException(ValidationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        // 无法拼接明细的错误信息，因为 Dubbo Consumer 抛出 ValidationException 异常时，是直接的字符串信息，且人类不可读
        return CommonResult.error(BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     *
     * 注意，它需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult<?> noHandlerFoundExceptionHandler(HttpServletRequest req, NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]", ex);
        return CommonResult.error(NOT_FOUND.getCode(), String.format("请求地址不存在:%s", ex.getRequestURL()));
    }


    /**
     * 处理 SpringMVC 请求方法不正确
     *
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return CommonResult.error(METHOD_NOT_ALLOWED.getCode(), String.format("请求方法不正确:%s", ex.getMessage()));
    }

    /**
     * 处理 Resilience4j 限流抛出的异常
     *
     * 不知道为什么这个会报错RequestNotPermitted
     */
    @ExceptionHandler(value = RequestNotPermitted.class)
    public CommonResult<?> requestNotPermittedExceptionHandler(HttpServletRequest req, Exception ex) {
        log.warn("[requestNotPermittedExceptionHandler][url({}) 访问过于频繁]", req.getRequestURL(), ex);
        return CommonResult.error(TOO_MANY_REQUESTS);
    }

    /**
     * 处理 Spring Security 权限不足的异常
     *
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     *
     * 不知道为什么这个AccessDeniedException会报错
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<?> accessDeniedExceptionHandler(HttpServletRequest req, Exception ex) {
        log.warn("[accessDeniedExceptionHandler][userId({}) 无法访问 url({})]", WebFrameworkUtils.getLoginUserId(req),
                req.getRequestURL(), ex);
        return CommonResult.error(FORBIDDEN);
    }

    /**
     * 处理业务异常 ServiceException
     *
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException ex) {
        log.info("[serviceExceptionHandler]", ex);
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        log.error("[defaultExceptionHandler]", ex);
        // 插入异常日志
        this.createExceptionLog(req, ex);
        // 返回 ERROR CommonResult
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonResult<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(),"文件大小超出" + maxFileSize + "限制, 请压缩或降低文件质量! ");
    }

    private void createExceptionLog(HttpServletRequest req, Throwable ex) {
        // 插入错误日志
        ApiErrorLog errorLog = new ApiErrorLog();
        try{
            // 初始化 errorLog
            initExceptionLog(errorLog, req, ex);
            // 执行插入 errorLog
            apiErrorLogFrameworkService.createApiErrorLog(errorLog);
        }catch (Throwable throwable){
            log.error("[createExceptionLog][url({}) log({}) 发生异常]", req.getRequestURI(),
                    JSONUtil.toJsonStr(errorLog), throwable);
        }
    }

    private void initExceptionLog(ApiErrorLog errorLog, HttpServletRequest request, Throwable throwable) {
        // 处理用户信息
        errorLog.setUserId(WebFrameworkUtils.getLoginUserId(request));
        errorLog.setUserType(WebFrameworkUtils.getLoginUserType(request));
        // 设置异常字段
        errorLog.setExceptionName(throwable.getClass().getName());
        errorLog.setExceptionMessage(ExceptionUtil.getMessage(throwable));
        errorLog.setExceptionRootCauseMessage(ExceptionUtil.getRootCauseMessage(throwable));
        errorLog.setExceptionStackTrace(ExceptionUtils.getStackTrace(throwable));
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        Assert.notEmpty(stackTraceElements, "异常 stackTraceElements 不能为空");
        StackTraceElement stackTraceElement = stackTraceElements[0];
        errorLog.setExceptionClassName(stackTraceElement.getClassName());
        errorLog.setExceptionFileName(stackTraceElement.getFileName());
        errorLog.setExceptionMethodName(stackTraceElement.getMethodName());
        errorLog.setExceptionLineNumber(stackTraceElement.getLineNumber());
        // 设置其它字段
        errorLog.setTraceId(TracerUtils.getTraceId());
        errorLog.setApplicationName(applicationName);
        errorLog.setRequestUrl(request.getRequestURI());
        Map<String, Object> requestParams = MapUtil.<String, Object>builder()
                .put("query", ServletUtil.getParamMap(request))
                .put("body", ServletUtil.getBody(request)).build();
        errorLog.setRequestParams(JSONUtil.toJsonStr(requestParams));
        errorLog.setRequestMethod(request.getMethod());
        errorLog.setUserAgent(ServletUtils.getUserAgent(request));
        errorLog.setUserIp(ServletUtil.getClientIP(request));
        errorLog.setExceptionTime(LocalDateTime.now());
    }


}
