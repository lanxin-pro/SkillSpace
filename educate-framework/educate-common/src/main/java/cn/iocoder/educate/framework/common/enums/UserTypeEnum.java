package cn.iocoder.educate.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
  * @Author: 董伟豪
  * @Date: 2023/5/7 11:55
 * 全局用户类型枚举
  */
@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    /**
     * 面向 c 端，普通用户
     */
    MEMBER(1, "会员"),

    /**
     *  面向 b 端，管理后台
     */
    ADMIN(2, "管理员");

    /**
     * 类型
     */
    private final Integer value;

    /**
     * 类型名
     */
    private final String name;
}
