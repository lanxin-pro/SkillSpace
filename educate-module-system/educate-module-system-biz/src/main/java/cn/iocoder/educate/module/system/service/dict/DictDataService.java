package cn.iocoder.educate.module.system.service.dict;

import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;

import java.util.List;

/**
 * 字典数据 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:11
 */
public interface DictDataService {

    /**
     * 获得字典数据列表
     *
     * @return 字典数据全列表
     */
    List<DictDataDO> getDictDataList();

    /**
     * 获得指定字典类型的数据数量
     *
     * @param dictType 字典类型
     * @return 数据数量
     */
    long countByDictType(String dictType);

}
