package cn.iocoder.educate.module.infra.controller.admin.logger.vo.apierrorlog;

import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/22 9:59
 */
@Schema(description = "管理后台 - API 错误日志 Excel 导出 Request VO,参数和 ApiErrorLogPageReqVO 是一致的")
@Data
public class ApiErrorLogExportReqVO {

    @Schema(description = "用户编号", example = "666")
    private Long userId;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

    @Schema(description = "应用名", example = "dashboard")
    private String applicationName;

    @Schema(description = "请求地址", example = "/xx/yy")
    private String requestUrl;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "异常发生时间")
    private LocalDateTime[] exceptionTime;

    @Schema(description = "处理状态", example = "0")
    private Integer processStatus;

}
