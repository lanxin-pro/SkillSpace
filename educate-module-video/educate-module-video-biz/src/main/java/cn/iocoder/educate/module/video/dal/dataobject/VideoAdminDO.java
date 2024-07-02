package cn.iocoder.educate.module.video.dal.dataobject;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 后台管理视频列表 DO
 *
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:44
 */
@TableName(value = "video_admin_file", autoResultMap = true)
@KeySequence("video_admin_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoAdminDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 模块类型 1：视频 2：漫画 3：动漫
     */
    private Byte moduleType;

    /**
     * 用户编号
     */
    private String upAccno;

    /**
     * up视频审核表d
     */
    private Integer videoSupplyId;

    /**
     * md5校验码
     */
    private String identifier;

    /**
     * 视频创作者头像
     */
    private String upAvatar;

    /**
     * 视频总收益
     */
    private BigDecimal income;

    /**
     * 时长类型 1：长视频 2：短视频
     */
    private Byte durationType;

    /**
     * 视频创造者（发布者）
     */
    private String upNickname;

    /**
     * 视频创造者（发布者）ID
     */
    private Long upUserId;

    /**
     * 屏幕类型 1：横屏 2：竖屏
     */
    private Byte orientation;

    /**
     * 打开时间
     */
    private LocalDateTime openTime;

    /**
     * 导过来的数据是否同步到es中
     */
    // private Boolean esSync;

    /**
     * 1 视频后台管理系统  2陌陌系统数据 3：移动端创作者中心 4：移动端推片 5：管理端UP主
     */
    private Byte datasource;

    /**
     * 超清分辨率
     */
    private String superReso;

    /**
     * 高清分辨率
     */
    private String highReso;

    /**
     * 标清分辨率
     */
    private String stanReso;

    /**
     * 评分
     */
    private String score;

    /**
     * 视频番号
     */
    private String copyRightCode;

    /**
     * 视频编号
     */
    private String videoCode;

    /**
     * 主分类名
     */
    private String categoryPname;

    /**
     * 主分类IDS
     */
    private String categoryPid;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 作者头像
     */
    private String actorAvatars;

    /**
     * 作者昵称
     */
    private String actorNames;

    /**
     * 作者ID
     */
    private String actorIds;

    /**
     * 字幕 0：无中字幕 1：有中字幕
     */
    private Byte subtitleFlag;

    /**
     * 0：无码 1有码
     */
    private Byte mosaicFlag;

    /**
     * 1：大陆 2：日本 3：韩国 4：欧美 5：台湾 6：港澳
     */
    private Byte region;

    /**
     * 关联标签多个标签以空格隔开
     */
    private String tagList;

    /**
     * 所属分类
     */
    private String categoryId;

    /**
     * 评论数量
     */
    private Integer commentNumber;

    /**
     * 播放数量
     */
    private Long playNumber;

    /**
     * 当前点赞数
     */
    private Long likeNumber;

    /**
     * 开启状态 1：开启 0：未开启
     */
    private Byte enableStatus;

    /**
     * VIP等级优惠价
     */
    private Integer discountPrice;

    /**
     * 免费为0，VIP填用户等级，收费为收费值
     */
    private Integer price;

    /**
     * 默认1=免费,2=VIP,3=收费
     */
    private Byte priceType;

    /**
     * 时长：秒
     */
    private Integer duration;

    /**
     * 内容类型 1：AV 2：三级 3：动漫 4：自拍
     */
    private Byte contentType;

    /**
     * 封面
     */
    private String cover;

    /**
     * 预览视频地址
     */
    private String preview;

    /**
     * super大小
     */
    private Long superSize;

    /**
     * high大小
     */
    private Long highSize;

    /**
     * stan大小
     */
    private Long stanSize;

    /**
     * 超清视频地址
     */
    private String superUrl;

    /**
     * 高清视频地址
     */
    private String highUrl;

    /**
     * 标清视频地址
     */
    private String stanUrl;

    /**
     * 大小
     */
    private Long size;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 简介：须限定字数500
     */
    private String intro;

    /**
     * 标题：须限定字数200
     */
    private String title;

}
