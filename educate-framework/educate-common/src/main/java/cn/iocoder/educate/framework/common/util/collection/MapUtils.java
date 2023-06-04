package cn.iocoder.educate.framework.common.util.collection;

import cn.iocoder.educate.framework.common.core.KeyValue;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

/**
 * Map 工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/3 18:17
 */
public class MapUtils {

    public static <K, V> Map<K, V> convertMap(List<KeyValue<K, V>> keyValues) {
        Map<K, V> map = Maps.newLinkedHashMapWithExpectedSize(keyValues.size());
        keyValues.forEach(keyValue -> map.put(keyValue.getKey(), keyValue.getValue()));
        return map;
    }
}
