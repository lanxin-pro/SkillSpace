package cn.iocoder.educate.module.system.service.dict;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypeUpdateReqVO;
import cn.iocoder.educate.module.system.convert.dict.DictTypeConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.educate.module.system.dal.mysql.dict.DictTypeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:14
 */
@Slf4j
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictDataService dictDataService;

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageResult<DictTypeDO> getDictTypePage(DictTypePageReqVO dictTypePageReqVO) {
        return dictTypeMapper.selectPage(dictTypePageReqVO);
    }

    @Override
    public Long createDictType(DictTypeCreateReqVO dictTypeCreateReqVO) {
        // 校验正确性
        validateDictTypeForCreateOrUpdate(null, dictTypeCreateReqVO.getName(), dictTypeCreateReqVO.getType());

        // 插入字典类型
        DictTypeDO dictType = DictTypeConvert.INSTANCE.convert(dictTypeCreateReqVO);
        // 唯一索引，避免 null 值
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY);
        dictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(DictTypeUpdateReqVO dictTypeUpdateReqVO) {
        // 校验正确性
        validateDictTypeForCreateOrUpdate(dictTypeUpdateReqVO.getId(), dictTypeUpdateReqVO.getName(), null);

        // 更新字典类型
        DictTypeDO dictTypeDO = DictTypeConvert.INSTANCE.convert(dictTypeUpdateReqVO);
        dictTypeMapper.updateById(dictTypeDO);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        DictTypeDO dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dictDataService.countByDictType(dictType.getType()) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public DictTypeDO getDictType(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public DictTypeDO getDictType(String type) {
        return dictTypeMapper.selectByType(type);
    }

    @Override
    public List<DictTypeDO> getDictTypeList() {
        return dictTypeMapper.selectList(new LambdaQueryWrapper<>());
    }

    private void validateDictTypeForCreateOrUpdate(Long id, String name, String type) {
        // 校验自己存在
        validateDictTypeExists(id);
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(id, name);
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(id, type);
    }

    void validateDictTypeNameUnique(Long id, String name) {
        DictTypeDO dictType = dictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE);
        }
    }

    void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        DictTypeDO dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    DictTypeDO validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        DictTypeDO dictType = dictTypeMapper.selectById(id);
        if (dictType == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
