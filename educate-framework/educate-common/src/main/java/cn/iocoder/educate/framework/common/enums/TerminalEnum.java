package cn.iocoder.educate.framework.common.enums;

import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 终端的枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/12/5 20:49
 */
@RequiredArgsConstructor
@Getter
public enum TerminalEnum implements IntArrayValuable {

    WECHAT_MINI_PROGRAM(10, "微信小程序"),
    WECHAT_WAP(11, "微信公众号"),
    H5(20, "H5 网页"),
    APP(31, "手机 App"),
            ;

    public static final int[] ARRAYS = Arrays.stream(values())
            .mapToInt(TerminalEnum::getTerminal)
            .toArray();

    /**
     * 终端
     */
    private final Integer terminal;
    /**
     * 终端名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
