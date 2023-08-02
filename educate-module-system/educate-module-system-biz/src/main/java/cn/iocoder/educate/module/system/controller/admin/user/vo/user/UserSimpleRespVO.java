package cn.iocoder.educate.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/30 11:22
 */
@Schema(description = "管理后台 - 用户精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "蓝欣")
    private String nickname;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "运维部门")
    private String deptName;

}
