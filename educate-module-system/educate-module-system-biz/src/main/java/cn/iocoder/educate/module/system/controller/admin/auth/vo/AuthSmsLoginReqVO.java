package cn.iocoder.educate.module.system.controller.admin.auth.vo;

import cn.iocoder.educate.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/21 11:31
 */
@Schema(description = "管理后台 - 短信验证码的登录 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSmsLoginReqVO {

    @Schema(description = "手机号", required = true, example = "1377108234")
    @NotEmpty(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "短信验证码", required = true, example = "1024")
    @NotEmpty(message = "验证码不能为空")
    private String code;

}
