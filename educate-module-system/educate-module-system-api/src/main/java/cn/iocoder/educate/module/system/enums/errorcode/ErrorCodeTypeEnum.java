package cn.iocoder.educate.module.system.enums.errorcode;

import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 错误码的类型枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 10:41
 */
@AllArgsConstructor
@Getter
public enum ErrorCodeTypeEnum implements IntArrayValuable {

    /**
     * 自动生成
     */
    AUTO_GENERATION(1),

    /**
     * 手动编辑
     */
    MANUAL_OPERATION(2);

    public static final int[] ARRAYS = Arrays.stream(values())
            .mapToInt(ErrorCodeTypeEnum::getType)
            .toArray();

    /**
     * 类型
     */
    private final Integer type;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
