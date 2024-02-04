package cn.iocoder.educate.module.course.controller.admin.section.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author j-sentinel
 * @date 2024/2/4 13:45
 */
@Schema(description = "管理后台 - 课程信息 Response VO")
@Data
@ToString(callSuper = true)
public class CourseSectionReqVO {

    @Schema(description = "课程节的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20988")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "章节标题")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "章节内容")
    private String content;

    @Schema(description = "标签")
    private Set<String> tags;

    @Schema(description = "主题缩略描述", example = "随便")
    private String description;

    @Schema(description = "html内容")
    private String htmlContent;

    @Schema(description = "作者头像")
    private String avatar;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "发布状态", example = "2")
    private Integer status;

    @Schema(description = "用户", example = "4667")
    private Long userId;

    @Schema(description = "章节ID", example = "10512")
    private Long pid;

    @Schema(description = "笔记ID", example = "26087")
    @NotEmpty(message = "指派的课程不能为空")
    private Long courseId;

    @Schema(description = "排序")
    private Integer sorted;

    @Schema(description = "课程时长")
    private String courseTimer;

    @Schema(description = "课程时长的大小")
    private String courseTimerSize;

    @Schema(description = "课程视频地址")
    private String videoLink;

    @Schema(description = "课程视频文件大小")
    private String videoFileSize;

    @Schema(description = "视频标签")
    private String videoTag;

    @Schema(description = "视频首图")
    private String videoFirstImage;

    @Schema(description = "视频id", example = "25812")
    private String videoId;

    @Schema(description = "视频播放器高度")
    private String videoPlayerHeight;

    @Schema(description = "视频mp4")
    private String videoMp4;

    @Schema(description = "视频分类id", example = "20782")
    private String videoCategoryId;

    @Schema(description = "视频分类名称")
    private String videoCategoryTitle;

    @Schema(description = "视频播放器宽度")
    private String videoPlayerWidth;

    @Schema(description = "视频格式的地址")
    private String videoSwfLink;

    @Schema(description = "视频原地址")
    private String videoOriginal;

    @Schema(description = "视频df")
    private String videoDf;

    @Schema(description = "视频发送者")
    private String videoSeed;

    @Schema(description = "视频状态", example = "1")
    private String videoStatus;

    @Schema(description = "视频倍速")
    private String videoTimes;

    @Schema(description = "视频内容")
    private String videoContext;

    @Schema(description = "是否免费")
    private Integer isFree;

    @Schema(description = "小结信息")
    private List<CourseSectionRespVO> sectionList;

}
