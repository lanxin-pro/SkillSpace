package cn.iocoder.educate.framework.common.enums;

import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
  * @Author: 董伟豪
  * @Date: 2023/5/7 11:55
 * 全局用户类型枚举
  */
@AllArgsConstructor
@Getter
public enum UserTypeEnum implements IntArrayValuable {

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

    public static final int[] ARRAYS =
            Arrays.stream(values()).mapToInt(UserTypeEnum::getValue).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
