package cn.iocoder.educate.module.infra.covert.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTableRespVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.DatabaseTableRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.apache.ibatis.type.JdbcType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 12:12
 */
@Mapper
public interface CodegenConvert {

    CodegenConvert INSTANCE = Mappers.getMapper(CodegenConvert.class);

    @Mappings({
            @Mapping(source = "name", target = "tableName"),
            @Mapping(source = "comment", target = "tableComment"),
    })
    CodegenTableDO convert(TableInfo bean);

    /**
     * 使用下面的转换方法{@link CodegenConvert.convert(TableField bean) }
     * @param list
     * @return
     */
    List<CodegenColumnDO> convertList(List<TableField> list);

    /**
     * qualifiedByName 可以自定义转换方法
     * @param bean
     * @return
     */
    @Mappings({
            @Mapping(source = "name", target = "columnName"),
            @Mapping(source = "metaInfo.jdbcType", target = "dataType", qualifiedByName = "getDataType"),
            @Mapping(source = "comment", target = "columnComment"),
            @Mapping(source = "metaInfo.nullable", target = "nullable"),
            @Mapping(source = "keyFlag", target = "primaryKey"),
            @Mapping(source = "keyIdentityFlag", target = "autoIncrement"),
            @Mapping(source = "columnType.type", target = "javaType"),
            @Mapping(source = "propertyName", target = "javaField"),
    })
    CodegenColumnDO convert(TableField bean);

    @Named("getDataType")
    default String getDataType(JdbcType jdbcType) {
        return jdbcType.name();
    }

    PageResult<CodegenTableRespVO> convertPage(PageResult<CodegenTableDO> page);

    List<DatabaseTableRespVO> convertList04(List<TableInfo> list);

}
