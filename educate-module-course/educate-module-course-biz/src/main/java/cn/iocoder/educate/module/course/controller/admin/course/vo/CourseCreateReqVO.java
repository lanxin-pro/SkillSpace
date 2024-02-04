package cn.iocoder.educate.module.course.controller.admin.course.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author j-sentinel
 * @date 2024/1/30 11:13
 */
@Schema(description = "管理后台 - 课程信息 Response VO")
@Data
@ToString(callSuper = true)
public class CourseCreateReqVO {

    @Schema(description = "主题标题")
    @NotEmpty(message = "主题标题不能为空")
    @Length(min = 1, max = 30, message = "主题标题长度为 1-30 位")
    private String title;

    @Schema(description = "主题内容")
    private String content;

    @Schema(description = "主题标签")
    private Set<String> tags;

    @Schema(description = "主题缩略描述", example = "啊对对对，你说的对")
    private String description;

    @Schema(description = "主题分类ID", example = "26399")
    private Long categoryId;

    @Schema(description = "0普通 1置顶")
    private Integer goTop;

    @Schema(description = "主题浏览次数")
    private Integer views;

    @Schema(description = "0不可以评论 1可以评论")
    private Integer comment;

    @Schema(description = "html内容")
    private String htmlContent;

    @Schema(description = "分类标题")
    private String categoryTitle;

    @Schema(description = "是否VIP 1 所有人免费 2 月VIP 3季度会员 4 年VIP 5超级VIP")
    private Integer vip;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "昵称", example = "李四")
    private String nickname;

    @Schema(description = "发布状态", example = "1")
    private Integer status;

    @Schema(description = "用户", example = "19924")
    private Long userId;

    @Schema(description = "封面图")
    private String img;

    @Schema(description = "收藏数")
    private Integer collects;

    @Schema(description = "评论数量")
    private Integer comments;

    @Schema(description = "课程时长")
    private String courseTimer;

    @Schema(description = "原始价格2499", example = "19195")
    private String price;

    @Schema(description = "真实价格1499", example = "23902")
    private String realPrice;

    @Schema(description = "课程类型 1 基础课  2 进阶课  4 面试课  3 实战课程", example = "1")
    private Integer courseType;

    @Schema(description = "排序")
    private Integer sorted;

    @Schema(description = "是否活动")
    private String beginer;

    @Schema(description = "是否最新")
    private Integer isNew;

    @Schema(description = "是否最热")
    private Integer isHot;

    @Schema(description = "是否上传")
    private Integer isPush;

}
