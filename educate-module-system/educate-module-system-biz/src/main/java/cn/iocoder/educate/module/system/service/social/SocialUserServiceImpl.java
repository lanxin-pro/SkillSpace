package cn.iocoder.educate.module.system.service.social;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.http.HttpUtils;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.social.core.EducateAuthRequestFactory;
import cn.iocoder.educate.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserBindDO;
import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.educate.module.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.educate.module.system.dal.mysql.social.SocialUserMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/7 9:10
 */
@Slf4j
@Service
@Validated
public class SocialUserServiceImpl implements SocialUserService{

    /**
     * 由于自定义了 EducateAuthRequestFactory 无法覆盖默认的 AuthRequestFactory，所以只能注入它
     */
    @Resource
    private EducateAuthRequestFactory educateAuthRequestFactory;

    @Resource
    private SocialUserMapper socialUserMapper;

    @Resource
    private SocialUserBindMapper socialUserBindMapper;

    @Override
    public String getAuthorizeUrl(Integer type, String redirectUri) {
        // 保存在数据库中的字段应该更加的准确
        String source = SocialTypeEnum.valueOfType(type).getSource();
        // 获得对应的 AuthRequest 实现
        AuthRequest authRequest = educateAuthRequestFactory.get(source);
        // 生成跳转地址
        String authorizeUri = authRequest.authorize(AuthStateUtils.createState());
        return HttpUtils.replaceUrlQuery(authorizeUri, "redirect_uri", redirectUri);
    }

    /**
     * @param userType 用户类型
     * @param type 社交平台的类型
     * @param code 授权码
     * @param state state
     * @return
     */
    @Override
    public Long getBindUserId(Integer userType, Integer type, String code, String state) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(type,code,state);
        Assert.notNull(socialUser,"社交用户不能为空");

        // 如果未绑定的社交用户，则无法自动登录，进行报错
        SocialUserBindDO socialUserBindDO = socialUserBindMapper.selectByUserTypeAndSocialUserId(userType,
                socialUser.getId());
        if (socialUserBindDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_THIRD_LOGIN_NOT_BIND);
        }
        return socialUserBindDO.getUserId();
    }

    /**
     * code授权码只能用一次，页面在初始化的使用已经被使用了，但是我只需要把同样的code保存到数据库当中，接下来就可以继续使用了
     *
     * @param socialUserBindReqDTO 绑定信息
     */
    @Override
    public void bindSocialUser(SocialUserBindReqDTO socialUserBindReqDTO) {
        // 获得社交用户
        SocialUserDO socialUserDO = authSocialUser(socialUserBindReqDTO.getType(), socialUserBindReqDTO.getCode(),
                socialUserBindReqDTO.getState());
        Assert.notNull(socialUserDO,"社交用户不能为空");
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(socialUserBindReqDTO.getUserId()).userType(socialUserBindReqDTO.getUserType())
                .socialUserId(socialUserDO.getId()).socialType(socialUserDO.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
    }

    @Override
    public List<SocialUserDO> getSocialUserList(Long userId, Integer userType) {
        // 获得绑定
        List<SocialUserBindDO> socialUserBinds = socialUserBindMapper.selectListByUserIdAndUserType(userId,userType);
        if(CollUtil.isEmpty(socialUserBinds)){
            return Collections.emptyList();
        }
        Set<Long> collect = socialUserBinds
                .stream()
                .map(SocialUserBindDO::getSocialUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 获得社交用户
        return socialUserMapper.selectBatchIds(collect);
    }

    /**
     * code授权码只能用一次，页面在初始化的使用已经被使用了，但是我只需要把同样的code保存到数据库当中，接下来就可以继续使用了
     *
     * 1.页面刷新的时候每次都，从数据库中查询到的话(相同的type,code,state)，直接返回，不然我就使用code授权码来查询出钉钉的用户信息
     * 2.我重新扫码二维码有数据的时候，我应该更新用户信息，并且返回
     * 3.第一次扫码二维码的话，就是单纯的创建用户
     *
     * @param type 社交类型
     * @param code code 授权码
     * @param state state
     * @return 三方返回的社交用户信息
     */
    private SocialUserDO authSocialUser(Integer type, String code, String state) {
        // 优先从 DB 中获取，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAnState(type, code, state);
        if(socialUser != null){
            return socialUser;
        }
        // 请求获取（使用 code 授权码）
        AuthUser authUser = getAuthUser(type, code, state);
        Assert.notNull(authUser,"三方用户不能为空");
        // 保存到 DB 中
        // 使用type和解析后的code（openid）再次查询数据库
        socialUser = socialUserMapper.selectByTypeAndOpenid(type, authUser.getUuid());
        if(socialUser == null){
            socialUser = new SocialUserDO();
        }
        // 需要保存 code + state 字段，保证后续可查询
        socialUser.setType(type).setCode(code).setState(state)
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken())
                .setRawTokenInfo((JsonUtils.toJsonString(authUser.getToken())))
                .setNickname(authUser.getNickname()).setAvatar(authUser.getAvatar())
                .setRawUserInfo(JsonUtils.toJsonString(authUser.getRawUserInfo()));
        // 这个就说明数据库中没有查询到 -- openId和对应的type --
        if(socialUser.getId() == null){
            socialUserMapper.insert(socialUser);
        }else{
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }

    /**
     * 请求社交平台，获得授权的用户
     *
     * @param type 社交平台的类型
     * @param code 授权码
     * @param state 授权 state
     * @return 授权的用户
     */
    private AuthUser getAuthUser(Integer type, String code, String state) {
        AuthRequest authRequest = educateAuthRequestFactory.get(SocialTypeEnum.valueOfType(type).getSource());
        AuthCallback authCallback = AuthCallback.builder()
                .code(code).state(state).build();
        AuthResponse<?> authResponse = authRequest.login(authCallback);
        log.info("[getAuthUser][请求社交平台 type({}) request({}) response({})]", type,
                JsonUtils.toJsonString(authCallback), JsonUtils.toJsonString(authResponse));
        if (!authResponse.ok()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SOCIAL_USER_AUTH_FAILURE, authResponse.getMsg());
        }
        return (AuthUser) authResponse.getData();
    }
}
