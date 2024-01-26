package cn.iocoder.educate.framework.dict.core.util;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.common.util.cache.CacheUtils;
import cn.iocoder.educate.module.system.api.dict.DictDataApi;
import cn.iocoder.educate.module.system.api.dict.dto.DictDataRespDTO;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;

/**
 * 字典工具类
 *
 * @author j-sentinel
 * @date 2024/1/19 18:45
 */
@Slf4j
public class DictFrameworkUtils {

    private static DictDataApi dictDataApi;

    private static final DictDataRespDTO DICT_DATA_NULL = new DictDataRespDTO();

    /**
     * 针对 {@link #getDictDataLabel(String, String)} 的缓存
     */
    private static final LoadingCache<KeyValue<String, String>, DictDataRespDTO> GET_DICT_DATA_CACHE = CacheUtils
        .buildAsyncReloadingCache(
            // 过期时间 1 分钟
            Duration.ofMinutes(1L),
            new CacheLoader<KeyValue<String, String>, DictDataRespDTO>() {

                @Override
                public DictDataRespDTO load(KeyValue<String, String> key) throws Exception {
                    DictDataRespDTO dictData = dictDataApi.getDictData(key.getKey(), key.getValue());
                    return ObjectUtil.defaultIfNull(dictData, DICT_DATA_NULL);
                }

            });

    public static void init(DictDataApi dictDataApi) {
        // 赋值给当前作用域，来充当后续的工具类
        DictFrameworkUtils.dictDataApi = dictDataApi;
        log.info("[init][初始化 DictFrameworkUtils 成功]");
    }

    @SneakyThrows
    public static String getDictDataLabel(String dictType, String value) {
        DictDataRespDTO dictDataRespDTO = GET_DICT_DATA_CACHE.get(new KeyValue<>(dictType, value));
        return dictDataRespDTO.getLabel();
    }

}
