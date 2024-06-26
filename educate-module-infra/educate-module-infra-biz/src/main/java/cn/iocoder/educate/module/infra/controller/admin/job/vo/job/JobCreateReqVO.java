package cn.iocoder.educate.module.infra.controller.admin.job.vo.job;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:47
 */
@Schema(description = "管理后台 - 定时任务创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobCreateReqVO extends JobBaseVO {

    @Schema(description = "处理器的名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "sysUserSessionTimeoutJob")
    @NotNull(message = "处理器的名字不能为空")
    private String handlerName;

}
