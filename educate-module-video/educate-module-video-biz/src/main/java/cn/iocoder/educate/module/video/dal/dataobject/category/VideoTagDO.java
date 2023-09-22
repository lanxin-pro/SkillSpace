package cn.iocoder.educate.module.video.dal.dataobject.category;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:56
 */
@TableName(value = "video_tag", autoResultMap = true)
@KeySequence("video_tag") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VideoTagDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签集合
     */
    private String tagList;

    /**
     * 开启状态 1：开启 0：未开启
     */
    private Integer enableStatus;

}
