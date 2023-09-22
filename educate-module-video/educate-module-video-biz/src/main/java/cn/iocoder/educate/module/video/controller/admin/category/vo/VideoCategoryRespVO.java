package cn.iocoder.educate.module.video.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 面板分类 VideoCategory VO")
public class VideoCategoryRespVO {

    @Schema(description = "面板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String categoryName;

    @Schema(description = "父分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer parentId;

    @Schema(description = "排序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sortNum;

    @Schema(description = "开启状态 1：开启 0：未开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer enableStatus;

    @Schema(description = "是否绑定面板", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer bindcount;

    @Schema(description = "是否展示首页  1 绑定 0 不绑定", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer bindFlag;

    @Schema(description = "是否展示首页  1 展示 0 不展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer defaultIndex;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private LocalDateTime updateTime;

    @Schema(description = "查询子类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private List<VideoCategoryRespVO> childrenList;

}
