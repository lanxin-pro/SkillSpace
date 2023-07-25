package cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 14:26
 */
@Schema(description = "管理后台 - 数据库的表定义 Response VO")
@Data
public class DatabaseTableRespVO {

    @Schema(description = "表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "lanxin")
    private String name;

    @Schema(description = "表描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "蓝欣")
    private String comment;

}
