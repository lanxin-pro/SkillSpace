package cn.iocoder.educate.framework.dict.core.util;

import cn.iocoder.educate.module.system.api.dict.DictDataApi;
import lombok.extern.slf4j.Slf4j;

/**
 * 字典工具类
 *
 * @author j-sentinel
 * @date 2024/1/19 18:45
 */
@Slf4j
public class DictFrameworkUtils {

    private static DictDataApi dictDataApi;

    public static void init(DictDataApi dictDataApi) {
        // 赋值给当前作用域，来充当后续的工具类
        DictFrameworkUtils.dictDataApi = dictDataApi;
        log.info("[init][初始化 DictFrameworkUtils 成功]");
    }



}
