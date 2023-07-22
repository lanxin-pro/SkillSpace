package cn.iocoder.educate.module.system.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/22 15:00
 */
@Schema(description = "管理后台 - 菜单更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuUpdateReqVO extends MenuBaseVO {

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "菜单编号不能为空")
    private Long id;

}
