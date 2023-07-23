package cn.iocoder.educate.module.infra.covert.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTableRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 12:12
 */
@Mapper
public interface CodegenConvert {

    CodegenConvert INSTANCE = Mappers.getMapper(CodegenConvert.class);

    PageResult<CodegenTableRespVO> convertPage(PageResult<CodegenTableDO> page);

}
