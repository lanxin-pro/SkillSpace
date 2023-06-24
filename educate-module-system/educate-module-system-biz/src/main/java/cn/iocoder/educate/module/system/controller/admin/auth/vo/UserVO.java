package cn.iocoder.educate.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/22 16:57
 */
@Schema(description = "用户信息 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道源码")
    private String nickname;

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://www.iocoder.cn/xx.jpg")
    private String avatar;
}
