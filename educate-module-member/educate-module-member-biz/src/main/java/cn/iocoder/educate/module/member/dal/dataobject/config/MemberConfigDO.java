package cn.iocoder.educate.module.member.dal.dataobject.config;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 会员配置 DO
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:33
 */
@TableName(value = "member_config", autoResultMap = true)
@KeySequence("member_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberConfigDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 积分抵扣开关
     */
    private Boolean pointTradeDeductEnable;

    /**
     * 积分抵扣，单位：分
     *
     * 1 积分抵扣多少分
     */
    private Integer pointTradeDeductUnitPrice;

    /**
     * 积分抵扣最大值
     */
    private Integer pointTradeDeductMaxPrice;

    /**
     * 1 元赠送多少分
     */
    private Integer pointTradeGivePoint;

}
