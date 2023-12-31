package cn.iocoder.educate.module.trade.controller.app.base.spu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品 SPU 基础 Response VO
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 15:33
 */
@Data
public class AppProductSpuBaseRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "商品 SPU 名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "商品主图地址", example = "https://www.iocoder.cn/xx.png")
    private String picUrl;

}
