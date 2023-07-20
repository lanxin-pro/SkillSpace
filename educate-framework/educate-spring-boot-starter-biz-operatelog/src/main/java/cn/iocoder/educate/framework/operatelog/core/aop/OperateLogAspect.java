package cn.iocoder.educate.framework.operatelog.core.aop;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.util.monitor.TracerUtils;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.framework.operatelog.core.enums.OperateTypeEnum;
import cn.iocoder.educate.framework.operatelog.core.service.OperateLogFrameworkService;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 10:56
 *
 * 拦截使用 @OperateLog 注解，如果满足条件，则生成操作日志。
 * 满足如下任一条件，则会进行记录：
 * 1. 使用 @ApiOperation + 非 @GetMapping
 * 2. 使用 @OperateLog 注解
 *
 * 但是，如果声明 @OperateLog 注解时，将 enable 属性设置为 false 时，强制不记录。
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    @Resource
    private OperateLogFrameworkService operateLogFrameworkService;

    @Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Operation operation) throws Throwable {
        // 获取OperateLog
        OperateLog operateLog = getMethodAnnotation(proceedingJoinPoint,OperateLog.class);
        return around0(proceedingJoinPoint,operation,operateLog);
    }

    private Object around0(ProceedingJoinPoint proceedingJoinPoint, Operation operation,
                           OperateLog operateLog) throws Throwable {
        // 目前，只有管理员，才记录操作日志！所以非管理员，直接调用，不进行记录
        Integer loginUserType = WebFrameworkUtils.getLoginUserType();
        if(!ObjectUtil.equal(loginUserType, UserTypeEnum.ADMIN.getValue())){
            return proceedingJoinPoint.proceed();
        }
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        try {
            // 执行原有方法
            Object result = proceedingJoinPoint.proceed();
            // 记录正常执行时的操作日志
            this.log(proceedingJoinPoint,operateLog,operation,startTime,result,null);
            return result;
        }catch (Exception exception){
            this.log(proceedingJoinPoint, operateLog, operation, startTime, null, exception);
            throw exception;
        }
    }

    private void log(ProceedingJoinPoint joinPoint,
                     OperateLog operateLog,
                     Operation operation,
                     LocalDateTime startTime, Object result, Throwable exception) {
        try{
            // 判断不记录的情况
            if(!isLogEnable(joinPoint,operateLog)){
                return;
            }
            // 真正记录操作日志
            this.log0(joinPoint, operateLog, operation, startTime, result, exception);
        }catch (Exception ex){
            log.error("[log][记录操作日志时，发生异常，其中参数是 joinPoint({}) operateLog({}) apiOperation({}) result({}) exception({}) ]",
                    joinPoint, operateLog, operation, result, exception, ex);
        }
    }

    /**
     * 调用api接口来保存日志的记录
     * @param joinPoint
     * @param operateLog
     * @param operation
     * @param startTime
     * @param result
     * @param exception
     */
    private void log0(ProceedingJoinPoint joinPoint, OperateLog operateLog, Operation operation,
                      LocalDateTime startTime, Object result, Throwable exception) {
        cn.iocoder.educate.framework.operatelog.core.service.OperateLog operateLogObj = new cn.iocoder.educate.framework.operatelog.core.service.OperateLog();
        operateLogObj.setTraceId(TracerUtils.getTraceId());
        operateLogObj.setStartTime(startTime);
        // 补充用户信息
        fillUserFields(operateLogObj);
        // 补全模块信息
        fillModuleFields(operateLogObj,joinPoint,operateLog,operation);
        // 补全请求信息
        fillRequestFields(operateLogObj);
        // 补全方法信息
        fillMethodFields(operateLogObj,joinPoint,operateLog,startTime,result,exception);
        // 异步记录日志
        operateLogFrameworkService.createOperateLog(operateLogObj);
    }

    private void fillMethodFields(cn.iocoder.educate.framework.operatelog.core.service.OperateLog operateLogObj,
                                  ProceedingJoinPoint joinPoint, OperateLog operateLog, LocalDateTime startTime,
                                  Object result, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        operateLogObj.setJavaMethod(methodSignature.toString());
        // 方法参数的获取
        if(operateLog == null || operateLog.logArgs()){
            operateLogObj.setJavaMethodArgs(obtainMethodArgs(joinPoint));
        }
        if (operateLog == null || operateLog.logResultData()) {
            operateLogObj.setResultData(obtainResultData(result));
        }
        operateLogObj.setDuration((int) (LocalDateTimeUtil.between(startTime,LocalDateTime.now()).toMillis()));
        if(result instanceof CommonResult){
            CommonResult<?> commonResult = (CommonResult<?>) result;
            operateLogObj.setResultCode(commonResult.getCode());
            operateLogObj.setResultMsg(commonResult.getMsg());
        }else{
            operateLogObj.setResultCode(GlobalErrorCodeConstants.SUCCESS.getCode());
        }
        // （异常）处理 resultCode 和 resultMsg 字段
        if (exception != null) {
            operateLogObj.setResultCode(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode());
            operateLogObj.setResultMsg(ExceptionUtil.getRootCauseMessage(exception));
        }
    }

    private static void fillRequestFields(cn.iocoder.educate.framework.operatelog.core.service.OperateLog operateLogObj) {
        HttpServletRequest request = ServletUtils.getRequest();
        if(request == null){
            return;
        }
        // 补全请求信息
        operateLogObj.setRequestMethod(request.getMethod());
        operateLogObj.setRequestUrl(request.getRequestURI());
        operateLogObj.setUserIp(ServletUtil.getClientIP(request));
        operateLogObj.setUserAgent(ServletUtils.getUserAgent(request));
    }

    private void fillModuleFields(cn.iocoder.educate.framework.operatelog.core.service.OperateLog operateLogObj,
                                  ProceedingJoinPoint joinPoint, OperateLog operateLog, Operation operation) {
        // --------- 操作模块 module 属性 ---------
        if(operateLog != null){
            operateLogObj.setModule(operateLogObj.getModule());
        }
        // module是空 拿类上面的tag来作为moduleName
        if(StrUtil.isEmpty(operateLogObj.getModule())){
            Tag tag = extractedTag(joinPoint);
            if(tag != null){
                // 优先读取 @Tag 的 name 属性
                if(StrUtil.isNotEmpty(tag.name())){
                    operateLogObj.setModule(tag.name());
                }
                // 没有的话，读取 @API 的 description 属性
                if(StrUtil.isEmpty(operateLogObj.getModule()) && StrUtil.isNotEmpty(tag.description())){
                    operateLogObj.setModule(tag.description());
                }
            }
        }
        // --------- name属性 ---------
        if(operateLog != null){
            operateLogObj.setName(operateLog.name());
        }
        // 如果我没有写@OperateLog注解的情况
        if(StrUtil.isEmpty(operateLogObj.getName()) && operation != null){
            operateLogObj.setName(operation.summary());
        }
        // --------- type 属性 ---------
        if(operateLog != null && ObjectUtil.isNotEmpty(operateLog.type())){
            operateLogObj.setType(operateLogObj.getType());
        }
        if(operateLogObj.getType() == null){
            // 请求类型
            RequestMethod requestMethods = obtainFirstMatchRequestMethod(extractedRequestMapping(joinPoint));
            OperateTypeEnum operateLogType = convertOperateLogType(requestMethods);
            operateLogObj.setType(operateLogType != null ? operateLogType.getType() : null);
        }
    }

    private void fillUserFields(cn.iocoder.educate.framework.operatelog.core.service.OperateLog operateLogObj) {
        operateLogObj.setUserId(WebFrameworkUtils.getLoginUserId());
        operateLogObj.setUserType(WebFrameworkUtils.getLoginUserType());
    }

    /**
     * 把requestMethods转换枚举
     * @param requestMethods
     * @return
     */
    private static OperateTypeEnum convertOperateLogType(RequestMethod requestMethods) {
        if(requestMethods == null){
            return null;
        }
        switch (requestMethods){
            case GET:
                return OperateTypeEnum.GET;
            case POST:
                return OperateTypeEnum.CREATE;
            case PUT:
                return OperateTypeEnum.UPDATE;
            case DELETE:
                return OperateTypeEnum.DELETE;
            default:
                return OperateTypeEnum.GET;
        }
    }

    private String obtainResultData(Object result) {
        // TODO 提升：结果脱敏和忽略
        if (result instanceof CommonResult) {
            result = ((CommonResult<?>) result).getData();
        }
        return JSONUtil.toJsonStr(result);
    }

    /**
     * 获取方法调用时传递的所有参数，并将参数名和参数值组装成 JSON 字符串返回
     * @param joinPoint
     * @return
     */
    private String obtainMethodArgs(ProceedingJoinPoint joinPoint) {
        // TODO 提升：参数脱敏和忽略
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        // 拼接参数
        Map<String, Object> args = Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // 循环遍历每一个参数，如果该参数需要忽略，则使用字符串"[ignore]"代替，否则记录参数的值；避免和 null 混在一起
            args.put(argName, !isIgnoreArgs(argValue) ? argValue : "[ignore]");
        }
        return JSONUtil.toJsonStr(args);
    }

    /**
     * 支持的方法级别，有可能有很多种类，但是我只想要一个
     * @param requestMethods
     * @return
     */
    private static RequestMethod obtainFirstMatchRequestMethod(RequestMethod[] requestMethods) {
        if (ObjectUtil.isEmpty(requestMethods)) {
            return null;
        }
        // 优先，匹配最优的 POST、PUT、DELETE
        RequestMethod result = obtainFirstLogRequestMethod(requestMethods);
        if(result != null){
            return result;
        }
        // 兜底，获得第一个
        return requestMethods[0];
    }

    /**
     * 使用 findFirst 方法，找到符合特定条件的第一个请求方法，如果不存在，则返回 null，主要是排除GET请求
     *
     * @param requestMethods
     * @return
     */
    private static RequestMethod obtainFirstLogRequestMethod(RequestMethod[] requestMethods) {
        if(ObjectUtil.isEmpty(requestMethods)){
            return null;
        }
        return Arrays.stream(requestMethods).filter(requestMethod ->
                requestMethod == RequestMethod.POST
                        || requestMethod == RequestMethod.PUT
                        || requestMethod == RequestMethod.DELETE)
                .findFirst().orElse(null);
    }

    /**
     * 获取类上面的 @Tag 注解
     * @param joinPoint
     */
    private Tag extractedTag(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getAnnotation(Tag.class);
    }

    /**
     * 获取@OperateLog注解，前提就是我得有@Operation
     *
     * @param proceedingJoinPoint
     * @return
     */
    private <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint proceedingJoinPoint, Class<T> annotationClass) {
        return ((MethodSignature) proceedingJoinPoint.getSignature())
                .getMethod().getAnnotation(annotationClass);
    }

    /**
     * 获取方法上面的 @RequestMapping
     * 如何存在的话，直接获取请求类型
     * 支持的方法级别
     * @param joinPoint
     */
    private RequestMethod[] extractedRequestMapping(ProceedingJoinPoint joinPoint) {
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(
                ((MethodSignature) joinPoint.getSignature()).getMethod(), RequestMapping.class);
        return requestMapping != null ? requestMapping.method() : new RequestMethod[]{};
    }

    /**
     * 判断operateLog的enable状态
     *
     * @param joinPoint
     * @param operateLog
     * @return
     */
    private static boolean isLogEnable(ProceedingJoinPoint joinPoint, OperateLog operateLog) {
        // 有 @OperateLog 注解的情况
        if(operateLog != null){
            return operateLog.enable();
        }
        // 没有 @ApiOperation 注解的情况下，只记录 POST、PUT、DELETE 的情况
        return obtainFirstLogRequestMethod(obtainRequestMethod(joinPoint)) != null;
    }

    /**
     * 获取请求类型集合
     *
     * @param joinPoint
     * @return
     */
    private static RequestMethod[] obtainRequestMethod(ProceedingJoinPoint joinPoint) {
            // 使用 Spring 的工具类，可以处理 @RequestMapping 别名注解
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(
                ((MethodSignature) joinPoint.getSignature()).getMethod(), RequestMapping.class);
        return requestMapping != null ? requestMapping.method() : new RequestMethod[]{};
    }

    private static boolean isIgnoreArgs(Object object) {
        Class<?> clazz = object.getClass();
        // 处理数组的情况
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object))
                    .anyMatch(index -> isIgnoreArgs(Array.get(object, index)));
        }
        // 递归，处理数组、Collection、Map 的情况
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream()
                    .anyMatch((Predicate<Object>) OperateLogAspect::isIgnoreArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreArgs(((Map<?, ?>) object).values());
        }
        // obj
        return object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult;
    }

}
