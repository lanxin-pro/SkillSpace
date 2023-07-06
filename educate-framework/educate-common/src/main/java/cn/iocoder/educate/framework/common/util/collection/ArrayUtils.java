package cn.iocoder.educate.framework.common.util.collection;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 12:47
 */
public class ArrayUtils {

    public static <T> T get(T[] array, int index) {
        if (null == array || index >= array.length) {
            return null;
        }
        return array[index];
    }
}
