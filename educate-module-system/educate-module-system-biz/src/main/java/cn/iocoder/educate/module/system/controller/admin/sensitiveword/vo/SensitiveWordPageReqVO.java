package cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo;


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
 * @Date: 2023/8/21 8:03
 */
@Schema(description = "管理后台 - 敏感词分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SensitiveWordPageReqVO extends PageParam {

    @Schema(description = "敏感词", example = "敏感词")
    private String name;

    @Schema(description = "标签", example = "短信,评论")
    private String tag;

    @Schema(description = "状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
