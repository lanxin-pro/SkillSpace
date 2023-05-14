package cn.iocoder.educate.framework.common.enums;

import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:08
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IntArrayValuable {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getStatus).toArray();

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return new int[0];
    }
}
