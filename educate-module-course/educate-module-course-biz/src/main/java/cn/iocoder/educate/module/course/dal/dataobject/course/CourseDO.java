package cn.iocoder.educate.module.course.dal.dataobject.course;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.mybatis.core.type.JsonStringSetTypeHandler;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Set;

/**
 * 我们对实体类的某个字段自定义了typeHandler，一定要开启autoResultMap=true才能生效
 *
 * @author j-sentinel
 * @date 2024/1/28 11:35
 */
@TableName(value = "online_course", autoResultMap = true)
@KeySequence("online_course_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 主题标题
     */
    private String title;

    /**
     * 主题内容
     */
    private String content;

    /**
     * 主题标签
     */
    @TableField(typeHandler = JsonStringSetTypeHandler.class)
    private Set<String> tags;

    /**
     * 主题缩略描述
     */
    private String description;

    /**
     * 主题分类ID
     */
    private Long categoryId;

    /**
     * 0普通 1置顶
     */
    private Integer goTop;

    /**
     * 主题浏览次数
     */
    private Integer views;

    /**
     * 0不可以评论 1可以评论
     */
    private Integer comment;

    /**
     * html内容
     */
    private String htmlContent;

    /**
     * 分类标题
     */
    private String categoryTitle;

    /**
     * 是否VIP 1 所有人免费 2 月VIP 3季度会员 4 年VIP 5超级VIP
     */
    private Integer vip;

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
     * 封面图
     */
    private String img;

    /**
     * 收藏数
     */
    private Integer collects;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 课程时长
     */
    private String courseTimer;

    /**
     * 原始价格2499
     */
    private String price;

    /**
     * 真实价格1499
     */
    private String realPrice;

    /**
     * 课程类型 1 基础课  2 进阶课  4 面试课  3 实战课程
     */
    private Integer courseType;

    /**
     * 排序
     */
    private Integer sorted;

    /**
     * 是否活动
     */
    private String beginer;

    /**
     * 是否最新
     */
    private Integer isNew;

    /**
     * 是否最热
     */
    private Integer isHot;

    /**
     * 是否上传
     */
    private Integer isPush;

}
