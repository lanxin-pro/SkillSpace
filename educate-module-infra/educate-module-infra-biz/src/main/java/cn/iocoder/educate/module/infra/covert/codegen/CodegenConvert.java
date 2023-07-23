package cn.iocoder.educate.module.infra.covert.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTableRespVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.DatabaseTableRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 12:12
 */
@Mapper
public interface CodegenConvert {

    CodegenConvert INSTANCE = Mappers.getMapper(CodegenConvert.class);

    PageResult<CodegenTableRespVO> convertPage(PageResult<CodegenTableDO> page);

    List<DatabaseTableRespVO> convertList04(List<TableInfo> list);

}
