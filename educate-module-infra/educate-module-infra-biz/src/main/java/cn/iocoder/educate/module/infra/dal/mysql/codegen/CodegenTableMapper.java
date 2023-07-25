package cn.iocoder.educate.module.infra.dal.mysql.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;


/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 12:16
 */
@Mapper
public interface CodegenTableMapper extends BaseMapper<CodegenTableDO> {

    default PageResult<CodegenTableDO> selectPage(CodegenTablePageReqVO pageReqVO) {
        LambdaQueryWrapper<CodegenTableDO> codegenTableDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        codegenTableDOLambdaQueryWrapper
                .like(StringUtils.hasText(pageReqVO.getTableName())
                        ,CodegenTableDO::getTableName,pageReqVO.getTableName())
                .like(StringUtils.hasText(pageReqVO.getTableComment())
                        ,CodegenTableDO::getTableComment,pageReqVO.getTableComment())
                .like(StringUtils.hasText(pageReqVO.getClassName())
                        ,CodegenTableDO::getClassName,pageReqVO.getClassName())
                .between(null != ArrayUtils.get(pageReqVO.getCreateTime(),0) &&
                        ArrayUtils.get(pageReqVO.getCreateTime(),1) != null,
                        CodegenTableDO::getCreateTime,
                        ArrayUtils.get(pageReqVO.getCreateTime(),0),
                        ArrayUtils.get(pageReqVO.getCreateTime(),1));
        Page<CodegenTableDO> page = new Page<>(pageReqVO.getPageNo(),pageReqVO.getPageSize());
        Page<CodegenTableDO> codegenTableDOPageDo = this.selectPage(page, codegenTableDOLambdaQueryWrapper);
        return new PageResult<>(codegenTableDOPageDo.getRecords(),codegenTableDOPageDo.getTotal());
    }

    default CodegenTableDO selectByTableNameAndDataSourceConfigId(String name, Long dataSourceConfigId){
        LambdaQueryWrapper<CodegenTableDO> codegenTableDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        codegenTableDOLambdaQueryWrapper.eq(CodegenTableDO::getTableName,name)
                .eq(CodegenTableDO::getDataSourceConfigId,dataSourceConfigId);
        return selectOne(codegenTableDOLambdaQueryWrapper);
    }

}
