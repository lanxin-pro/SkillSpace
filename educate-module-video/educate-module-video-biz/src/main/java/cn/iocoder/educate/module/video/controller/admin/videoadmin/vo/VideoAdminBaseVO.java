package cn.iocoder.educate.module.video.controller.admin.videoadmin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知公告 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 *
 * @Author: j-sentinel
 * @Date: 2023/9/21 21:14
 */
@Data
public class VideoAdminBaseVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer id;

}
