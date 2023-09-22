package cn.iocoder.educate.module.video.controller.admin.category.vo.tag;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:53
 */
@Schema(description = "管理后台 - 通知公告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoTagReqVo extends PageParam {

    @Schema(description = "标签组名或标签名", example = "1")
    private String name;

    @Schema(description = "搜索关键词", example = "1")
    private String keyword;

    @Schema(description = "开启状态 1：开启 0：未开启", example = "1")
    private Integer enableStatus;

}
