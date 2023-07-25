package cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table;

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
 * @Date: 2023/7/23 11:09
 */
@Schema(description = "管理后台 - 表定义分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodegenTablePageReqVO extends PageParam {

    @Schema(description = "表名称,模糊匹配", example = "lanxin")
    private String tableName;

    @Schema(description = "表描述,模糊匹配", example = "蓝欣")
    private String tableComment;

    @Schema(description = "实体,模糊匹配", example = "Yudao")
    private String className;

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
