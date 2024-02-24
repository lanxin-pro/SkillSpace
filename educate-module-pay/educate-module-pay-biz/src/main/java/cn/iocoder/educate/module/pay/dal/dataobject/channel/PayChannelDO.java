package cn.iocoder.educate.module.pay.dal.dataobject.channel;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.pay.core.client.PayClientConfig;
import cn.iocoder.educate.framework.pay.core.enums.channel.PayChannelEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

/**
 * @author j-sentinel
 * @date 2024/2/18 15:13
 */
@TableName(value = "pay_channel", autoResultMap = true)
@KeySequence("pay_channel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayChannelDO extends BaseDO {

    /**
     * 渠道编号，数据库自增
     */
    private Long id;

    /**
     * 渠道编码
     *
     * 枚举 {@link PayChannelEnum}
     */
    private String code;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 渠道费率，单位：百分比
     */
    private Double feeRate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 应用编号
     *
     * 关联 {@link PayAppDO#getId()}
     */
    private Long appId;

    /**
     * 支付渠道配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PayClientConfig config;

}
