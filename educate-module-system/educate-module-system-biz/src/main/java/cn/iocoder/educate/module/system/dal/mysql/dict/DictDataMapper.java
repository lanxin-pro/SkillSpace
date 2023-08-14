package cn.iocoder.educate.module.system.dal.mysql.dict;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:12
 */
@Mapper
public interface DictDataMapper extends BaseMapper<DictDataDO> {

    default List<DictDataDO> selectList(){
        return this.selectList(new LambdaQueryWrapper<>());
    }

    default long selectCountByDictType(String dictType){
        LambdaQueryWrapper<DictDataDO> dictDataDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictDataDOLambdaQueryWrapper.eq(DictDataDO::getDictType,dictType);
        return selectCount(dictDataDOLambdaQueryWrapper);
    }

    default DictDataDO selectByDictTypeAndValue(String dictType, String value){
        LambdaQueryWrapper<DictDataDO> dictDataDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictDataDOLambdaQueryWrapper.eq(DictDataDO::getDictType,dictType)
                .eq(DictDataDO::getValue,value);
        return this.selectOne(dictDataDOLambdaQueryWrapper);
    }

    default PageResult<DictDataDO> selectPage(DictDataPageReqVO dictDataPageReqVO) {
        LambdaQueryWrapper<DictDataDO> dictDataDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictDataDOLambdaQueryWrapper
                .like(StringUtils.hasText(dictDataPageReqVO.getLabel()),
                        DictDataDO::getLabel,dictDataPageReqVO.getLabel())
                .eq(ObjUtil.isNotEmpty(dictDataPageReqVO.getDictType()),
                        DictDataDO::getDictType,dictDataPageReqVO.getDictType())
                .eq(ObjUtil.isNotEmpty(dictDataPageReqVO.getStatus()),
                        DictDataDO::getStatus,dictDataPageReqVO.getStatus())
                // 先根据type排序，然后根据sort排序
                .orderByDesc(Arrays.asList(DictDataDO::getDictType, DictDataDO::getSort));
        Page<DictDataDO> page = new Page<>(dictDataPageReqVO.getPageNo(), dictDataPageReqVO.getPageSize());
        Page<DictDataDO> dictDataDOPage = this.selectPage(page, dictDataDOLambdaQueryWrapper);
        return new PageResult<>(dictDataDOPage.getRecords(),dictDataDOPage.getTotal());
    }

}
