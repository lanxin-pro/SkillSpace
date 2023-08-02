package cn.iocoder.educate.module.system.controller.admin.dept.vo.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/2 10:53
 */
@Schema(description = "管理后台 - 岗位信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostRespVO extends PostBaseVO {

    @Schema(description = "岗位序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

}
