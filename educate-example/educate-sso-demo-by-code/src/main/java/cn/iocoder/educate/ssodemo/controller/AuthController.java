package cn.iocoder.educate.ssodemo.controller;

import cn.iocoder.educate.ssodemo.client.OAuth2Client;
import cn.iocoder.educate.ssodemo.client.dto.CommonResult;
import cn.iocoder.educate.ssodemo.client.dto.oauth2.OAuth2AccessTokenRespDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/15 14:19
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private OAuth2Client oauth2Client;

    /**
     * 使用 code 访问令牌，获得访问令牌
     *
     * @param code 授权码
     * @param redirectUri 重定向 URI
     * @return 访问令牌；注意，实际项目中，最好创建对应的 ResponseVO 类，只返回必要的字段
     */
    @PostMapping("/login-by-code")
    public CommonResult<OAuth2AccessTokenRespDTO> loginByCode(@RequestParam("code") String code,
                                                              @RequestParam("redirectUri") String redirectUri) {
        return oauth2Client.postAccessToken(code, redirectUri);
    }

    /**
     * 使用刷新令牌，获得（刷新）访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 访问令牌；注意，实际项目中，最好创建对应的 ResponseVO 类，只返回必要的字段
     */
    @PostMapping("/refresh-token")
    public CommonResult<OAuth2AccessTokenRespDTO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return oauth2Client.refreshToken(refreshToken);
    }

}
