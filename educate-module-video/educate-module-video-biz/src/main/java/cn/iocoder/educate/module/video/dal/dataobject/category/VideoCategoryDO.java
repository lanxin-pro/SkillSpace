package cn.iocoder.educate.module.video.dal.dataobject.category;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:29
 */
@TableName(value = "video_category", autoResultMap = true)
@KeySequence("video_category") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VideoCategoryDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类ID
     */
    private Integer parentId;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 开启状态 1：开启 0：未开启
     */
    private Integer enableStatus;

    /**
     * 是否展示首页  1 展示 0 不展示
     */
    private Integer defaultIndex;

    /**
     * 绑定数量
     */
    @TableField(exist = false)
    private Integer bindcount;

    /**
     * 是否绑定面板 1 绑定 0未绑定
     */
    private Integer bindFlag;

}
