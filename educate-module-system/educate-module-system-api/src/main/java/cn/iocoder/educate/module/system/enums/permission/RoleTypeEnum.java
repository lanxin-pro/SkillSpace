package cn.iocoder.educate.module.system.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/10 9:59
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /**
     * 内置角色
     */
    SYSTEM(1),
    /**
     * 自定义角色
     */
    CUSTOM(2);

    private final Integer type;

}
