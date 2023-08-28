package cn.iocoder.educate.module.video.controller.admin.videoadmin.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:41
 */
@Schema(description = "管理后台 - 视频详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VideoAdminPageReqVO extends PageParam {

    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "过滤的类型")
    private Integer type;

    @Schema(description = "视频编号")
    private String videoCode;

    @Schema(description = "面板ID")
    private Integer panelId;

    @Schema(description = "内容类型 1：AV 2：三级 3：动漫 4：自拍")
    private Integer contentType;

    @Schema(description = "所属二级分类")
    private String categoryId;

    @Schema(description = "所属二级分类名称")
    private String categoryName;

    @Schema(description = "所属一分类")
    private String categoryPid;

    @Schema(description = "所属一级类名称")
    private String categoryPname;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "状态开始时间")
    private String openStartTime;

    @Schema(description = "状态结束时间")
    private String openEndTime;

    @Schema(description = "开启状态 1：开启 0：未开启")
    private Integer enableStatus;

    @Schema(description = "1：大陆 2：日本 3：韩国 4：欧美 5：台湾 6：港澳")
    private Integer region;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "0：无码 1有码")
    private Integer mosaicFlag;

    @Schema(description = "字幕 0：无中字幕 1：有中字幕")
    private Integer subtitleFlag;

    @Schema(description = "默认1=免费,2=VIP,3=收费(金币)")
    private Integer priceType;

    @Schema(description = "是否同步ES")
    private Integer esSync;

    @Schema(description = "1 视频后台管理系统  2陌陌系统数据")
    private Integer datasource;

    @Schema(description = "视频屏幕类型 1：横屏 2：竖屏")
    private Integer orientation;

    @Schema(description = "开始时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
