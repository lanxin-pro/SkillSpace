package cn.iocoder.educate.module.course.controller.admin.chapter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author j-sentinel
 * @date 2024/1/30 15:33
 */
@Schema(description = "管理后台 - 课程信息更新 Request VO")
@Data
@ToString(callSuper = true)
public class CourseOnlineUpdateStatusReqVO {

    @Schema(description = "课程编号")
    private Long id;

    @Schema(description = "发布状态", example = "1")
    private Integer status;

    @Schema(description = "0不可以评论 1可以评论")
    private Integer comment;

    @Schema(description = "是否最热")
    private Integer isHot;

    @Schema(description = "课程类型 1 基础课  2 进阶课  4 面试课  3 实战课程", example = "1")
    private Integer courseType;

    // ============================== 下面是前端无法看到的，看后续业务 ==============================

    @Schema(description = "0普通 1置顶")
    private Integer goTop;

    @Schema(description = "是否最新")
    private Integer isNew;

    @Schema(description = "是否上传")
    private Integer isPush;

}
