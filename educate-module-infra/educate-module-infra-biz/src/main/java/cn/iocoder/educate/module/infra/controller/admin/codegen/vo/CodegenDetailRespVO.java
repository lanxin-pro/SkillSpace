package cn.iocoder.educate.module.infra.controller.admin.codegen.vo;

import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.column.CodegenColumnRespVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table.CodegenTableRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/25 14:00
 */
@Schema(description = "管理后台 - 代码生成表和字段的明细 Response VO")
@Data
public class CodegenDetailRespVO {

    @Schema(description = "表定义")
    private CodegenTableRespVO table;

    @Schema(description = "字段定义")
    private List<CodegenColumnRespVO> columns;

}
