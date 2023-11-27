package cn.iocoder.educate.module.member.dal.dataobject.tag;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 17:57
 */
@TableName("member_tag")
@KeySequence("member_tag_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberTagDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 标签名称
     */
    private String name;

}