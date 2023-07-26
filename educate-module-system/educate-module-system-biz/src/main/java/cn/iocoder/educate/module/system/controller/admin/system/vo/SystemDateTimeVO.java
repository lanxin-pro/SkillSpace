package cn.iocoder.educate.module.system.controller.admin.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 18:10
 */
@Schema(description = "系统配置基础接口 - 系统时间 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDateTimeVO {

    @Schema(description = "当前系统时间")
    private Long time;

}
