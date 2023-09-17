package cn.iocoder.educate.module.infra.controller.admin.job.vo.job;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/17 14:30
 */
@Schema(description = "管理后台 - 定时任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobPageReqVO extends PageParam {

    @Schema(description = "任务名称,模糊匹配", example = "测试任务")
    private String name;

    @Schema(description = "任务状态,参见 JobStatusEnum 枚举", example = "1")
    private Integer status;

    @Schema(description = "处理器的名字,模糊匹配", example = "sysUserSessionTimeoutJob")
    private String handlerName;

}
