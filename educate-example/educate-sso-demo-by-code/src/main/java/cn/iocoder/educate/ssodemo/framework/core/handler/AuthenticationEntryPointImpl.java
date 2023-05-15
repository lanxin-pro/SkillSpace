package cn.iocoder.educate.ssodemo.framework.core.handler;

import cn.iocoder.educate.ssodemo.client.dto.CommonResult;
import cn.iocoder.educate.ssodemo.framework.core.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 访问一个需要认证的 URL 资源，但是此时自己尚未认证（登录）的情况下
 * 返回 {@link GlobalErrorCodeConstants#UNAUTHORIZED} 错误码，从而使前端重定向到登录页
 *
 * @Author: j-sentinel
 * @Date: 2023/5/15 13:18
 */
@Slf4j
@Component
@SuppressWarnings("JavadocReference") // 忽略文档引用报错
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), authException);
        // 返回 401
        CommonResult<Object> result = new CommonResult<>();
        result.setCode(HttpStatus.UNAUTHORIZED.value());
        result.setMsg("账号未登录");
        ServletUtils.writeJSON(response, result);
    }
}
