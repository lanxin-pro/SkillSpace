package cn.iocoder.educate.module.system.dal.dataobject.dept;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/29 13:16
 */
@TableName("system_user_post")
@KeySequence("system_user_post_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPostDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;

    /**
     * 用户 ID
     *
     * 关联 {@link AdminUserDO#getId()}
     */
    private Long userId;

    /**
     * 角色 ID
     *
     * 关联 {@link PostDO#getId()}
     */
    private Long postId;

}
