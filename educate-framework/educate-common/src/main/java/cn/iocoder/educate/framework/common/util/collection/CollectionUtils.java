package cn.iocoder.educate.framework.common.util.collection;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * Collection 工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:46
 */
public class CollectionUtils {

    public static <T> T getFirst(List<T> from) {
        return !CollectionUtil.isEmpty(from) ? from.get(0) : null;
    }

}
