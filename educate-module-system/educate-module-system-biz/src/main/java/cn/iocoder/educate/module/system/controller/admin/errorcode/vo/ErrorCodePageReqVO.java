package cn.iocoder.educate.module.system.controller.admin.errorcode.vo;

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
 * @Date: 2023/8/20 10:34
 */
@Schema(description = "管理后台 - 错误码分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorCodePageReqVO extends PageParam {

    @Schema(description = "错误码类型,参见 ErrorCodeTypeEnum 枚举类", example = "1")
    private Integer type;

    @Schema(description = "应用名", example = "dashboard")
    private String applicationName;

    @Schema(description = "错误码编码", example = "1234")
    private Integer code;

    @Schema(description = "错误码错误提示", example = "帅气")
    private String message;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
