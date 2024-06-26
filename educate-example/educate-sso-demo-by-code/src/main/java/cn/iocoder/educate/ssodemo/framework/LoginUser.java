package cn.iocoder.educate.ssodemo.framework;

import lombok.Data;

import java.util.List;

/**
 *
 * 登录用户信息
 *
 * @Author: j-sentinel
 * @Date: 2023/5/15 16:43
 */
@Data
public class LoginUser {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 授权范围
     */
    private List<String> scopes;

    /**
     * 访问令牌
     */
    private String accessToken;
}
