package cn.iocoder.educate.module.course.dal.dataobject.section;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author j-sentinel
 * @date 2024/1/31 20:00
 */
@TableName("online_course_section")
@KeySequence("online_course_section_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseSectionDO extends BaseDO {

    /**
     * 课程节的编号
     */
    @TableId
    private Long id;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节内容
     */
    private String content;

    /**
     * 标签
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<String> tags;

    /**
     * 主题缩略描述
     */
    private String description;

    /**
     * html内容
     */
    private String htmlContent;

    /**
     * 作者头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 发布状态
     */
    private Integer status;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 章节ID
     */
    private Long pid;

    /**
     * 笔记ID
     */
    private Long courseId;

    /**
     * 排序
     */
    private Integer sorted;

    /**
     * 课程时长
     */
    private String courseTimer;

    /**
     * 课程时长的大小
     */
    private String courseTimerSize;

    /**
     * 课程视频地址
     */
    private String videoLink;

    /**
     * 课程视频文件大小
     */
    private String videoFileSize;

    /**
     * 视频标签
     */
    private String videoTag;

    /**
     * 视频首图
     */
    private String videoFirstImage;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频播放器高度
     */
    private String videoPlayerHeight;

    /**
     * 视频mp4
     */
    private String videoMp4;

    /**
     * 视频分类id
     */
    private String videoCategoryId;

    /**
     * 视频分类名称
     */
    private String videoCategoryTitle;

    /**
     * 视频播放器宽度
     */
    private String videoPlayerWidth;

    /**
     * 视频格式的地址
     */
    private String videoSwfLink;

    /**
     * 视频原地址
     */
    private String videoOriginal;

    /**
     * 视频df
     */
    private String videoDf;

    /**
     * 视频发送者
     */
    private String videoSeed;

    /**
     * 视频状态
     */
    private String videoStatus;

    /**
     * 视频倍速
     */
    private String videoTimes;

    /**
     * 视频内容
     */
    private String videoContext;

    /**
     * 是否免费
     */
    private Integer isFree;

}
