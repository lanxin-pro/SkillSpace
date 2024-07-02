package cn.iocoder.educate.module.course.dal.dataobject.category;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author j-sentinel
 * @date 2024/4/13 17:39
 */
@TableName(value = "kss_category", autoResultMap = true)
@KeySequence("kss_category") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
public class CategoryDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer status;

    private Integer sorted;

    private Integer isdelete;

    private Long pid;

}
