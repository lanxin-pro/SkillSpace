package cn.iocoder.educate.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:35
 */
@Schema(description = "管理后台 - 部门列表 Request VO")
@Data
public class DeptListReqVO {

    @Schema(description = "部门名称,模糊匹配", example = "蓝欣")
    private String name;

    @Schema(description = "展示状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;
}
