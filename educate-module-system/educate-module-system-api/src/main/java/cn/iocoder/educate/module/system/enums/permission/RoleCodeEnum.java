package cn.iocoder.educate.module.system.enums.permission;

import cn.iocoder.educate.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 角色标识枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/6/23 17:09
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "超级管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        // 字符串的比较并不只是equals()，可以把字符串转换为数组然后使用数组中的方法contains()
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }
}
