package cn.iocoder.educate.module.video.controller.admin.category.vo.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 面板分类 VideoCategory VO")
public class VideoTagRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer id;

    @Schema(description = "标签组名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String name;

    @Schema(description = "标签集合", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String tagList;

    @Schema(description = "开启状态 1：开启 0：未开启", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer enableStatus;

}
