package cn.iocoder.educate.module.system.controller.admin.time.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 16:30
 */
@Schema(description = "国家和时区")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeCountryTimeZoneVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "时区", example = "1")
    private String timeZone;

    @Schema(description = "国家", example = "1")
    private String country;

}
