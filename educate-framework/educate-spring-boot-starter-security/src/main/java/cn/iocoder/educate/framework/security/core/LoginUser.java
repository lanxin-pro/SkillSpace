package cn.iocoder.educate.framework.security.core;

import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import lombok.Data;
import java.util.List;

/**
 * 登录用户信息
 * @Author: j-sentinel
 * @Date: 2023/5/15 9:36
 */
@Data
public class LoginUser {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户类型
     *
     * 关联 {@link UserTypeEnum}
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
}
