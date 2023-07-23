package cn.iocoder.educate.module.infra.controller.admin.codegen;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTableRespVO;
import cn.iocoder.educate.module.infra.covert.codegen.CodegenConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.service.codegen.CodegenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 11:05
 */
@Tag(name = "管理后台 - 代码生成器")
@RestController
@RequestMapping("/infra/codegen")
@Validated
public class CodegenController {

    @Resource
    private CodegenService codegenService;

    @GetMapping("/table/page")
    @Operation(summary = "获得表定义分页")
    public CommonResult<PageResult<CodegenTableRespVO>> getCodeGenTablePage(@Valid CodegenTablePageReqVO pageReqVO) {
        PageResult<CodegenTableDO> pageResult = codegenService.getCodegenTablePage(pageReqVO);
        return success(CodegenConvert.INSTANCE.convertPage(pageResult));
    }

}
