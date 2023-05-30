package cn.iocoder.educate.framework.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Key Value 的键值对
 *
 * @Author: j-sentinel
 * @Date: 2023/5/26 12:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K,V> {

    private K key;

    private V value;
}
