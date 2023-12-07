package cn.iocoder.educate.module.member.service.auth;

import cn.hutool.core.lang.Assert;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.monitor.TracerUtils;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsLoginReqVO;
import cn.iocoder.educate.module.member.controller.app.auth.vo.AppAuthSmsSendReqVO;
import cn.iocoder.educate.module.member.convert.auth.AuthConvert;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import cn.iocoder.educate.module.system.api.logger.LoginLogApi;
import cn.iocoder.educate.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.educate.module.system.api.oauth2.OAuth2TokenApi;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.educate.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.educate.module.system.api.sms.SmsCodeApi;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.educate.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.educate.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.educate.module.system.enums.sms.SmsSceneEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.AUTH_MOBILE_USED;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.USER_MOBILE_NOT_EXISTS;

/**
 * 会员的认证 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/5 20:47
 */
@Service
@Slf4j
public class MemberAuthServiceImpl implements MemberAuthService {

    @Resource
    private LoginLogApi loginLogApi;

    @Resource
    private SmsCodeApi smsCodeApi;

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private OAuth2TokenApi oAuth2TokenApi;

    @Override
    public AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO appAuthSmsLoginReqVO, Integer terminal) {
        String userIp = ServletUtils.getClientIP();
        // 校验验证码
        smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(appAuthSmsLoginReqVO,
                // 钉钉测试
                SmsSceneEnum.MEMBER_DEBUG_MEMBER_LOGIN.getScene(),
                userIp));

        // 获得获得注册用户
        MemberUserDO user = memberUserService.createUserIfAbsent(appAuthSmsLoginReqVO.getMobile(), userIp, terminal);
        Assert.notNull(user, "获取用户失败，结果为空");

        // 如果 socialType 非空，说明需要绑定社交用户
        String openid = null;
        if (appAuthSmsLoginReqVO.getSocialType() != null) {

        }
        // TODO j-sentinel member的特供？？？
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, appAuthSmsLoginReqVO.getMobile(), LoginLogTypeEnum.LOGIN_SMS, openid);
    }

    @Override
    public void sendSmsCode(Long userId, AppAuthSmsSendReqVO appAuthSmsSendReqVO) {
        // 情况 1：如果是修改手机号场景，需要校验新手机号是否已经注册，如果注册说明不能使用该手机了
        if (Objects.equals(appAuthSmsSendReqVO.getScene(), SmsSceneEnum.MEMBER_DEBUG_UPDATE_MOBILE.getScene())) {
            MemberUserDO user = memberUserService.getUserByMobile(appAuthSmsSendReqVO.getMobile());
            // 如果查询出来不为空，那么就是注册过了
            if (user != null && !Objects.equals(user.getId(), userId)) {
                throw ServiceExceptionUtil.exception(AUTH_MOBILE_USED);
            }
        }
        // 情况 2：如果是重置密码场景，需要校验手机号是存在的
        if (Objects.equals(appAuthSmsSendReqVO.getScene(), SmsSceneEnum.MEMBER_DEBUG_RESET_PASSWORD.getScene())) {
            MemberUserDO  user= memberUserService.getUserByMobile(appAuthSmsSendReqVO.getMobile());
            if (user == null) {
                throw ServiceExceptionUtil.exception(USER_MOBILE_NOT_EXISTS);
            }
        }
        // 其他情况 3：
        SmsCodeSendReqDTO smsCodeSendReqDTO = AuthConvert.INSTANCE.convert(appAuthSmsSendReqVO);
        smsCodeSendReqDTO.setCreateIp(ServletUtils.getClientIP());
        // 执行发送
        smsCodeApi.sendSmsCode(smsCodeSendReqDTO);
    }

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(MemberUserDO user, String mobile,
                                                            LoginLogTypeEnum logType, String openid) {
        // 插入登陆日志
        createLoginLog(user.getId(), mobile, logType, LoginResultEnum.SUCCESS);
        // 创建 Token 令牌
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oAuth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(user.getId()).setUserType(getUserType().getValue())
                .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenRespDTO, openid);
    }

    private void createLoginLog(Long userId, String mobile, LoginLogTypeEnum logType, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(mobile);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogApi.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            memberUserService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }

}
