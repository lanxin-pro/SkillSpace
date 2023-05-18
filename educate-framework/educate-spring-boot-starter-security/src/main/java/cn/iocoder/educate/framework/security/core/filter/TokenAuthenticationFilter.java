package cn.iocoder.educate.framework.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.framework.security.config.SecurityProperties;
import cn.iocoder.educate.framework.security.core.LoginUser;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.educate.module.system.api.oauth2.OAuth2TokenApi;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/16 11:47
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private OAuth2TokenApi oauth2TokenApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        if(StrUtil.isNotEmpty(token)){
            Integer loginUserType = WebFrameworkUtils.getLoginUserType();
            // 基于 token 构建登录用户
            LoginUser loginUser = buildLoginUserByToken(token, loginUserType);
            // 设置当前用户
            if (loginUser != null) {
                SecurityFrameworkUtils.setLoginUser(loginUser, request);
            }
        }
        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token, Integer userType) {
        try {
            OAuth2AccessTokenCheckRespDTO accessToken = oauth2TokenApi.checkAccessToken(token);
            if (accessToken == null) {
                return null;
            }
            // 用户类型不匹配，无权限
            if (ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
                throw new AccessDeniedException("错误的用户类型");
            }
            // 构建登录用户
            return new LoginUser().setId(accessToken.getUserId()).setUserType(accessToken.getUserType())
                    .setTenantId(accessToken.getTenantId()).setScopes(accessToken.getScopes());
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }
}
