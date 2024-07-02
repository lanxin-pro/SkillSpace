package cn.iocoder.educate.module.system.enums.oauth2;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OAuth2 授权类型（模式）的枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/10/15 18:05
 */
@AllArgsConstructor
@Getter
public enum OAuth2GrantTypeEnum {

    /**
     * 密码模式
     */
    PASSWORD("password"),

    /**
     * 授权码模式
     */
    AUTHORIZATION_CODE("authorization_code"),

    /**
     * 简化模式
     */
    IMPLICIT("implicit"),

    /**
     * 客户端模式
     */
    CLIENT_CREDENTIALS("client_credentials"),

    /**
     * 刷新模式
     */
    REFRESH_TOKEN("refresh_token"),
    ;

    private final String grantType;

    public static OAuth2GrantTypeEnum getByGranType(String grantType) {
        return ArrayUtil.firstMatch(o -> o.getGrantType().equals(grantType), values());
    }

}
