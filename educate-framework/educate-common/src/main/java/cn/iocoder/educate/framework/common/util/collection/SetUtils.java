package cn.iocoder.educate.framework.common.util.collection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 14:00
 * Set 工具类
 */
public class SetUtils {

    /**
     * 被@SafeVarargs注解标注的方法必须是由static或者final修饰的方法。
     * @param objs
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... objs) {
        return new HashSet<>(Arrays.asList(objs));
    }
}
