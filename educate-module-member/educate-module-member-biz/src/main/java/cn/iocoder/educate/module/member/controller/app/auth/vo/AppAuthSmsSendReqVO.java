package cn.iocoder.educate.module.member.controller.app.auth.vo;

import cn.iocoder.educate.framework.common.validation.InEnum;
import cn.iocoder.educate.framework.common.validation.Mobile;
import cn.iocoder.educate.module.system.enums.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/6 22:22
 */
@Schema(description = "用户 APP - 发送手机验证码 Request VO")
@Data
@Accessors(chain = true)
public class AppAuthSmsSendReqVO {

    @Schema(description = "手机号", example = "15601691234")
    @Mobile
    private String mobile;

    @Schema(description = "发送场景,对应 SmsSceneEnum 枚举", example = "1")
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;

}
