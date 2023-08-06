package cn.iocoder.educate.module.system.service.dict;

import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.educate.module.system.dal.mysql.dict.DictDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * 字典数据 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:11
 */
@Service
@Slf4j
public class DictDataServiceImpl implements DictDataService {

    @Resource
    private DictDataMapper dictDataMapper;

    @Resource
    private DictTypeService dictTypeService;

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            // 首要排序
            .comparing(DictDataDO::getDictType)
            // 次要排序
            .thenComparingInt(DictDataDO::getSort);

    @Override
    public List<DictDataDO> getDictDataList() {
        List<DictDataDO> list = dictDataMapper.selectList();
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public long countByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

}
