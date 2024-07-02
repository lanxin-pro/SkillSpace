package cn.iocoder.educate.module.pay.controller.admin.demo.vo.transfer;

import cn.iocoder.educate.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.pay.enums.transfer.PayTransferTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author j-sentinel
 * @date 2024/4/5 15:03
 */
@Schema(description = "管理后台 - 示例转账单创建 Request VO")
@Data
public class PayDemoTransferCreateReqVO {

    @Schema(description = "转账类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "转账类型不能为空")
    @InEnum(PayTransferTypeEnum.class)
    private Integer type;

    @Schema(description = "转账金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "转账金额不能为空")
    @Min(value = 1, message = "转账金额必须大于零")
    private Integer price;

    @Schema(description = "收款人姓名", example = "test1")
    @NotBlank(message = "收款人姓名不能为空", groups = { PayTransferTypeEnum.Alipay.class })
    private String userName;

    // ========== 支付宝转账相关字段 ==========
    @Schema(description = "支付宝登录号,支持邮箱和手机号格式", example = "alic2724@@qq.com")
    @NotBlank(message = "支付宝登录号不能为空", groups = { PayTransferTypeEnum.Alipay.class })
    private String alipayLogonId;

    // ========== 微信转账相关字段 ==========
    @Schema(description = "微信 openId", example = "oLefc4g5Gxx")
    @NotBlank(message = "微信 openId 不能为空", groups = { PayTransferTypeEnum.WxPay.class })
    private String openid;

}
