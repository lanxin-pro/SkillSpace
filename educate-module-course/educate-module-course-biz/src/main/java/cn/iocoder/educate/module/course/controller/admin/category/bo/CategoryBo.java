package cn.iocoder.educate.module.course.controller.admin.category.bo;

import cn.iocoder.educate.framework.common.util.date.DateUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/4/12 14:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryBo implements Serializable {

    private Long id;

    private String title;

    private String description;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] updateTime;

    private Integer status;

    private Integer sorted;

    private Integer isdelete;

    private Long pid;

    private boolean edit;

    @TableField(exist = false)
    private List<CategoryBo> children;

}
