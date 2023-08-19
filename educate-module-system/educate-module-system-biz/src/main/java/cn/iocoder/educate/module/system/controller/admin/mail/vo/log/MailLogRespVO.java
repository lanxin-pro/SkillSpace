package cn.iocoder.educate.module.system.controller.admin.mail.vo.log;

import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 16:24
 */
@Schema(description = "管理后台 - 邮件日志 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailLogRespVO extends MailLogBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "31020")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "用户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "蓝")
    private String nickname;

}
