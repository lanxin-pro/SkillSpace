package cn.iocoder.educate.module.system.service.auth;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.util.monitor.TracerUtils;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.framework.common.util.validation.ValidationUtils;
import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.educate.module.system.convert.auth.AuthConvert;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.educate.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.educate.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.educate.module.system.service.logger.LoginLogService;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2TokenService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.Objects;
import static cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 20:24
 * Auth Service 实现类
 * OAuth2.0实际更多应用于三方登录，如果没有三方登录的业务需求，完全可以使用JWT来完成
 */
@Slf4j
@Service
public class AdminAuthServiceImpl implements AdminAuthService{

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private Validator validate;

    /**
     * 验证码的开关，默认为 true
     */
    @Value("${lanxin.captcha.enable:true}")
    private Boolean captchaEnable;

    @Override
    public AdminUserDO authenticate(String username, String password) {
        final LoginLogTypeEnum loginTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        // 校验账号是否存在
        AdminUserDO adminUserDO = adminUserService.getUserByUsername(username);
         if(ObjUtil.isEmpty(adminUserDO)){
            createLoginLog(null,username,loginTypeEnum,LoginResultEnum.BAD_CREDENTIALS);
            throw exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        boolean passwordMatch = adminUserService.isPasswordMatch(password, adminUserDO.getPassword());
        if(!passwordMatch){
            createLoginLog(adminUserDO.getId(), username, loginTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if(ObjectUtil.notEqual(adminUserDO.getStatus(), CommonStatusEnum.ENABLE.getStatus())){
            createLoginLog(adminUserDO.getId(), username, loginTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
        }
        return adminUserDO;
    }

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        // 校验验证码
        validateCaptcha(reqVO);
        // 使用账号密码登录
        AdminUserDO adminUserDO = authenticate(reqVO.getUsername(),reqVO.getPassword());

        // 如果 socialType 非空，说明需要绑定社交用户
        if(reqVO.getSocialType() != null){
            log.error("socialType登录暂时未开放");
            throw exception(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(adminUserDO.getId(), adminUserDO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum loginTypeEnum) {
        // 插入登陆日志
        createLoginLog(userId, username, loginTypeEnum, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, null);
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    /**
     * @auther xingyuv
     * @param authLoginReqVO
     */
    @VisibleForTesting
    private void validateCaptcha(AuthLoginReqVO authLoginReqVO){
        if(!captchaEnable){
            return;
        }
        // 校验验证码
        ValidationUtils.validate(validate,AuthLoginReqVO.CodeEnableGroup.class);
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(authLoginReqVO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        // 验证不通过
        if (!response.isSuccess()) {
            // 创建登录失败日志（验证码不正确)
            createLoginLog(null, authLoginReqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME,
                    LoginResultEnum.CAPTCHA_CODE_ERROR);
            log.error("错误：{}，错误为：{}",ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }


    /**
     * 记录日志
     * @param userId 用户id
     * @param username 用户名
     * @param logTypeEnum 登录日志的类型枚举
     * @param loginResult 登录结果的枚举类
     */
    private void createLoginLog(Long userId, String username,
                                LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            adminUserService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    /**
     * 因为我是后台管理员，所以可以类会非常常用，我就提取出来
     * @return
     */
    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }
}
