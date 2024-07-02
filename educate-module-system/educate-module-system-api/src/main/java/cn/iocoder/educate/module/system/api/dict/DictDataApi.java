package cn.iocoder.educate.module.system.api.dict;

import cn.iocoder.educate.module.system.api.dict.dto.DictDataRespDTO;

/**
 * 字典数据 API 接口
 *
 * @author j-sentinel
 * @date 2024/1/19 18:52
 */
public interface DictDataApi {

    /**
     * 获取指定的字典数据，从缓存中
     *
     * @param type 字典类型
     * @param value 字典数据值
     * @return 字典数据
     */
    DictDataRespDTO getDictData(String type, String value);

}
