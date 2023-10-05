package cn.iocoder.educate.module.video.controller.admin.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 附件分片上传返回的对象")
public class VideoFileMergeRespVO {

    @Schema(description = "视频url", example = "2")
    private String url;

    @Schema(description = "封面url")
    private String cover;

    @Schema(description = "时长：秒")
    private Integer duration;

}
