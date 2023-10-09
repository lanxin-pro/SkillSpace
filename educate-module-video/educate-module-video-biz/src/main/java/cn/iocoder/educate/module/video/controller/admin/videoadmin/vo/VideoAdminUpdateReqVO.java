package cn.iocoder.educate.module.video.controller.admin.videoadmin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 视频列表后台管理返回类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:03
 */
@Schema(description = "管理后台 - 视频详情分页 Request VO")
@Data
public class VideoAdminUpdateReqVO extends VideoAdminBaseVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "视频编号")
    private String videoCode;

    @Schema(description = "简介：须限定字数500")
    private String intro;

    @Schema(description = "视频地址")
    private String url;

    @Schema(description = "视频大小")
    private Long size;

    @Schema(description = "标清视频地址")
    private String stanUrl;

    @Schema(description = "高清视频地址")
    private String highUrl;

    @Schema(description = "超清视频地址")
    private String superUrl;

    @Schema(description = "标清视频大小")
    private Long stanSize;

    @Schema(description = "高清视频大小")
    private Long highSize;

    @Schema(description = "超清视频大小")
    private Long superSize;

    @Schema(description = "预览视频地址")
    private String preview;

    @Schema(description = "封面")
    @NotNull(message = "封面不能为空")
    private String cover;

    @Schema(description = "内容类型 1：AV 2：三级 3：动漫 4：自拍")
    private Integer contentType;

    @Schema(description = "时长：秒")
    private Integer duration;

    @Schema(description = "默认1=免费,2=VIP,3=收费(金币)")
    private Integer priceType;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "免费为0，VIP填用户等级，收费为收费值")
    private BigDecimal price;

    @Schema(description = "VIP等级优惠价")
    private BigDecimal discountPrice;

    @Schema(description = "开启状态 1：未开启 0：开启")
    private Integer enableStatus;

    @Schema(description = "当前点赞数")
    private Long likeNumber;

    @Schema(description = "播放数量")
    private Long playNumber;

    @Schema(description = "评论数量")
    private Integer commentNumber;

    @Schema(description = "所属二级分类")
    private String categoryId;

    @Schema(description = "所属二级分类名称")
    private String categoryName;

    @Schema(description = "所属一分类")
    private String categoryPid;

    @Schema(description = "所属一级类名称")
    private String categoryPname;

    @Schema(description = "关联标签多个标签以空格隔开")
    private String tagList;

    @Schema(description = "1：大陆 2：日本 3：韩国 4：欧美 5：台湾 6：港澳")
    private Integer region;

    @Schema(description = "0：无码 1有码")
    private Integer mosaicFlag;

    @Schema(description = "字幕 0：无中字幕 1：有中字幕")
    private Integer subtitleFlag;

    @Schema(description = "视频秘钥")
    private String secretKey;

    @Schema(description = "作者ID")
    private String actorIds;

    @Schema(description = "作者昵称")
    private String actorNames;

    @Schema(description = "作者头像")
    private String actorAvatars;

    @Schema(description = "创建人ID")
    protected Long createBy;

    @Schema(description = "更新人ID")
    protected Long updateBy;

    @Schema(description = "是否删除 1：已删除 0：未删除")
    protected Integer deleted;

    @Schema(description = "视频番号")
    private String copyRightCode;

    @Schema(description = "打分")
    private String score;

    @Schema(description = "标清分辨率")
    private String spReso1;

    @Schema(description = "超清分辨率")
    private String spReso2;

    @Schema(description = "高清分辨率")
    private String spReso3;

    @Schema(description = "来源")
    private Integer datasource;

    @Schema(description = "是否同步到es 0不同步  1同步")
    private Integer esSync;

    @Schema(description = "视频屏幕类型 1：横屏 2：竖屏")
    private Integer orientation;

}
