package cn.iocoder.educate.module.system.controller.admin.auth.vo;

import cn.iocoder.educate.framework.common.validation.InEnum;
import cn.iocoder.educate.framework.common.validation.Mobile;
import cn.iocoder.educate.module.system.enums.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 方便开发者进行枚举类型的校验，在需要校验某个值是否在指定的枚举类型中时
 * 只需要在对应的字段或方法上使用 @InEnum 注解即可。
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:15
 */
@Schema(description = "管理后台 - 发送手机验证码 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSmsSendReqVO {

    @Schema(description = "手机号", required = true, example = "15791746551")
    @NotEmpty(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "短信场景", required = true, example = "1")
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
}
