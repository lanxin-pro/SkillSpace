package cn.iocoder.educate.module.video.controller.admin.videoadmin.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:40
 */
@Schema(description = "管理后台 - 视频详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VideoAdminRespVo extends PageParam {

    @Schema(description = "模块类型 1：视频 2：漫画 3：动漫", example = "2")
    private Byte moduleType;

    @Schema(description = "用户编号")
    private String upAccno;

    @Schema(description = "up视频审核表d", example = "1735")
    private Integer videoSupplyId;

    @Schema(description = "md5校验码")
    private String identifier;

    @Schema(description = "视频创作者头像")
    private String upAvatar;

    @Schema(description = "视频总收益")
    private BigDecimal income;

    @Schema(description = "时长类型 1：长视频 2：短视频", example = "2")
    private Byte durationType;

    @Schema(description = "视频创造者（发布者）", example = "赵六")
    private String upNickname;

    @Schema(description = "视频创造者（发布者）ID", example = "19380")
    private Long upUserId;

    @Schema(description = "屏幕类型 1：横屏 2：竖屏")
    private Byte orientation;

    @Schema(description = "打开时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime openTime;

    @Schema(description = "导过来的数据是否同步到es中")
    private Boolean esSync;

    @Schema(description = "1 视频后台管理系统  2陌陌系统数据 3：移动端创作者中心 4：移动端推片 5：管理端UP主")
    private Byte datasource;

    @Schema(description = "超清分辨率")
    private String superReso;

    @Schema(description = "高清分辨率")
    private String highReso;

    @Schema(description = "标清分辨率")
    private String stanReso;

    @Schema(description = "评分")
    private String score;

    @Schema(description = "视频番号")
    private String copyRightCode;

    @Schema(description = "视频编号")
    private String videoCode;

    @Schema(description = "主分类名", example = "蓝欣")
    private String categoryPname;

    @Schema(description = "主分类IDS", example = "13545")
    private String categoryPid;

    @Schema(description = "分类名称", example = "李四")
    private String categoryName;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "密钥")
    private String secretKey;

    @Schema(description = "更新人ID")
    private Long updateBy;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;

    @Schema(description = "作者头像")
    private String actorAvatars;

    @Schema(description = "作者昵称")
    private String actorNames;

    @Schema(description = "作者ID")
    private String actorIds;

    @Schema(description = "字幕 0：无中字幕 1：有中字幕")
    private Byte subtitleFlag;

    @Schema(description = "0：无码 1有码")
    private Byte mosaicFlag;

    @Schema(description = "1：大陆 2：日本 3：韩国 4：欧美 5：台湾 6：港澳")
    private Byte region;

    @Schema(description = "关联标签多个标签以空格隔开")
    private String tagList;

    @Schema(description = "所属分类", example = "4752")
    private String categoryId;

    @Schema(description = "评论数量")
    private Integer commentNumber;

    @Schema(description = "播放数量")
    private Long playNumber;

    @Schema(description = "当前点赞数")
    private Long likeNumber;

    @Schema(description = "开启状态 1：开启 0：未开启", example = "2")
    private Byte enableStatus;

    @Schema(description = "VIP等级优惠价", example = "11298")
    private Integer discountPrice;

    @Schema(description = "免费为0，VIP填用户等级，收费为收费值", example = "14724")
    private Integer price;

    @Schema(description = "默认1=免费,2=VIP,3=收费", example = "2")
    private Byte priceType;

    @Schema(description = "时长：秒")
    private Integer duration;

    @Schema(description = "内容类型 1：AV 2：三级 3：动漫 4：自拍", example = "1")
    private Byte contentType;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "预览视频地址")
    private String preview;

    @Schema(description = "super大小")
    private Long superSize;

    @Schema(description = "high大小")
    private Long highSize;

    @Schema(description = "stan大小")
    private Long stanSize;

    @Schema(description = "超清视频地址", example = "https://www.baidu.com")
    private String superUrl;

    @Schema(description = "高清视频地址", example = "https://www.baidu.com")
    private String highUrl;

    @Schema(description = "标清视频地址", example = "https://www.baidu.com")
    private String stanUrl;

    @Schema(description = "大小")
    private Long size;

    @Schema(description = "视频地址", example = "https://www.baidu.com")
    private String url;

    @Schema(description = "简介：须限定字数500")
    private String intro;

    @Schema(description = "标题：须限定字数200")
    private String title;

}
