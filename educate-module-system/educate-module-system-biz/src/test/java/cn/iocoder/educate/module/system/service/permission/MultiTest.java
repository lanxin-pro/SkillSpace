package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.HashMultiset;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/23 12:03
 */
public class MultiTest {

    /**
     * 统计每个单词出现的次数
     */
    @Test
    void a(){
        String[] words = { "a","a","b","c","d","d" };
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(String word : words){
            // 在hashMap中没有查询到,所以我添加到hashMap当中，然后加1
            Integer count = hashMap.get(word);
            if(count == null){
                hashMap.put(word,1);
            }else{
                hashMap.put(word,count + 1);
            }
        }
        System.out.println("统计每个单词出现的次数：" + hashMap);
    }

    @Test
    void aMiltMap(){
        String[] words = { "a","a","b","c","d","d" };
        List<String> strings = Arrays.asList(words);
        HashMultiset<String> hashMultiset = HashMultiset.create(strings);
        System.out.println(hashMultiset.count("a"));
        System.out.println(hashMultiset.count("b"));
        System.out.println(hashMultiset.count("c"));


        // 如果你想要不重复元素集合，还可以直接转成 Set
        // Set<String> words = multiset.elementSet();
    }

}
