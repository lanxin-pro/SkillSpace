package cn.iocoder.educate.module.mp.controller.admin.statistics.vo;

import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * @Author: j-sentinel
 * @Date: 2023/9/2 20:46
 */
@Schema(description = "管理后台 - 获得统计数据 Request VO")
@Data
public class MpStatisticsGetReqVO {

    @Schema(description = "公众号账号的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "公众号账号的编号不能为空")
    private Long accountId;

    @Schema(description = "查询时间范围")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @NotNull(message = "查询时间范围不能为空")
    private LocalDateTime[] date;

}
