package cn.iocoder.educate.framework.common.util.object;

import java.util.Arrays;

/**
 * Object 工具类
 * 
 * @Author: j-sentinel
 * @Date: 2023/6/23 17:26
 */
public class ObjectUtils {

    public static boolean equalsAny(String code, String code1) {
        return Arrays.asList(code).contains(code1);
    }
}
