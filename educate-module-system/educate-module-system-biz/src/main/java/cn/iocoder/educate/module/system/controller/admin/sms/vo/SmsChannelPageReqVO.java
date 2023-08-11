package cn.iocoder.educate.module.system.controller.admin.sms.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/11 14:54
 */
@Schema(description = "管理后台 - 短信渠道分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SmsChannelPageReqVO extends PageParam {

    @Schema(description = "任务状态", example = "1")
    private Integer status;

    @Schema(description = "短信签名,模糊匹配", example = "芋道源码")
    private String signature;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
