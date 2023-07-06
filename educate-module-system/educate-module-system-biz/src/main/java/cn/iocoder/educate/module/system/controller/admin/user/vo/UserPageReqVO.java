package cn.iocoder.educate.module.system.controller.admin.user.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.h2.mvstore.DataUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 9:39
 */
@Schema(description = "管理后台 - 用户分页 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPageReqVO extends PageParam {

    @Schema(description = "用户账号,模糊匹配", example = "lanxin")
    private String username;

    @Schema(description = "手机号码,模糊匹配", example = "lanxin")
    private String mobile;

    @Schema(description = "展示状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "部门编号,同时筛选子部门", example = "1024")
    private Long deptId;

}
