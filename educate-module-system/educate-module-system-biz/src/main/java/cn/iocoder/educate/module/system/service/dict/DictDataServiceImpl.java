package cn.iocoder.educate.module.system.service.dict;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataUpdateReqVO;
import cn.iocoder.educate.module.system.convert.dict.DictDataConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.educate.module.system.dal.mysql.dict.DictDataMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
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

    @Override
    public Long createDictData(DictDataCreateReqVO dictDataCreateReqVO) {
        // 校验正确性
        validateDictDataForCreateOrUpdate(null, dictDataCreateReqVO.getValue(),
                dictDataCreateReqVO.getDictType());

        // 插入字典类型
        DictDataDO dictData = DictDataConvert.INSTANCE.convert(dictDataCreateReqVO);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataUpdateReqVO dictDataUpdateReqVO) {
        // 校验正确性
        validateDictDataForCreateOrUpdate(dictDataUpdateReqVO.getId(), dictDataUpdateReqVO.getValue(),
                dictDataUpdateReqVO.getDictType());
        // 更新字典类型
        DictDataDO dictDataDO = DictDataConvert.INSTANCE.convert(dictDataUpdateReqVO);
        dictDataMapper.updateById(dictDataDO);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    @Override
    public PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO dictDataPageReqVO) {
        return dictDataMapper.selectPage(dictDataPageReqVO);
    }

    @Override
    public DictDataDO getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    private void validateDictDataForCreateOrUpdate(Long id, String value, String dictType) {
        // 校验自己存在
        validateDictDataExists(id);
        // 校验字典类型有效
        validateDictTypeExists(dictType);
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(id, dictType, value);
    }

    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        DictDataDO dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_DATA_NOT_EXISTS);
        }
    }

    public void validateDictTypeExists(String type) {
        DictTypeDO dictType = dictTypeService.getDictType(type);
        if (dictType == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_NOT_EXISTS);
        }
        // 判断开启状态
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_NOT_ENABLE);
        }
    }

    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE);
        }
    }

}
