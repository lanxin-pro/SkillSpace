package cn.iocoder.educate.module.system.service.social;

import cn.iocoder.educate.framework.common.util.http.HttpUtils;
import cn.iocoder.educate.framework.social.core.EducateAuthRequestFactory;
import cn.iocoder.educate.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/7 9:10
 */
@Slf4j
@Service
@Validated
public class SocialUserServiceImpl implements SocialUserService{

    @Resource
    private EducateAuthRequestFactory educateAuthRequestFactory;

    @Override
    public String getAuthorizeUrl(Integer type, String redirectUri) {
        String source = SocialTypeEnum.valueOfType(type).getSource();
        // 获得对应的 AuthRequest 实现
        AuthRequest authRequest = educateAuthRequestFactory.get(source);
        // 生成跳转地址
        String authorizeUri = authRequest.authorize(AuthStateUtils.createState());
        return  HttpUtils.replaceUrlQuery(authorizeUri, "redirect_uri", redirectUri);
    }
}
