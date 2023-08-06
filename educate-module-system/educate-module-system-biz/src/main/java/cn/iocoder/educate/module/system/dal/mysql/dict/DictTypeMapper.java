package cn.iocoder.educate.module.system.dal.mysql.dict;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictTypeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:14
 */
@Mapper
public interface DictTypeMapper extends BaseMapper<DictTypeDO> {

    default PageResult<DictTypeDO> selectPage(DictTypePageReqVO dictTypePageReqVO) {
        LambdaQueryWrapper<DictTypeDO> dictTypeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictTypeDOLambdaQueryWrapper
                .like(StringUtils.hasText(dictTypePageReqVO.getName()), DictTypeDO::getName,dictTypePageReqVO.getName())
                .like(StringUtils.hasText(dictTypePageReqVO.getType()),DictTypeDO::getType,dictTypePageReqVO.getType())
                .eq(ObjectUtil.isNotEmpty(dictTypePageReqVO.getStatus()),DictTypeDO::getStatus,dictTypePageReqVO.getStatus())
                .between(null != ArrayUtils.get(dictTypePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(dictTypePageReqVO.getCreateTime(),1) != null,
                        DictTypeDO::getCreateTime,
                        ArrayUtils.get(dictTypePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(dictTypePageReqVO.getCreateTime(),1))
                .orderByDesc(DictTypeDO::getId);
        Page<DictTypeDO> page = new Page<>(dictTypePageReqVO.getPageNo(), dictTypePageReqVO.getPageSize());
        Page<DictTypeDO> dictTypeDOPage = this.selectPage(page, dictTypeDOLambdaQueryWrapper);
        return new PageResult(dictTypeDOPage.getRecords(),dictTypeDOPage.getTotal());
    }

    default DictTypeDO selectByName(String name){
        LambdaQueryWrapper<DictTypeDO> dictTypeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictTypeDOLambdaQueryWrapper.eq(DictTypeDO::getName,name);
        return this.selectOne(dictTypeDOLambdaQueryWrapper);
    }

    default DictTypeDO selectByType(String type){
        LambdaQueryWrapper<DictTypeDO> dictTypeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictTypeDOLambdaQueryWrapper.eq(DictTypeDO::getType,type);
        return this.selectOne(dictTypeDOLambdaQueryWrapper);
    }

    @Update("UPDATE system_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);

}
