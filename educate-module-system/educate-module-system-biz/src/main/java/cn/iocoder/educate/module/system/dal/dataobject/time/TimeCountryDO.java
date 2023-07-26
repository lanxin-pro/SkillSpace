package cn.iocoder.educate.module.system.dal.dataobject.time;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 16:34
 */
@TableName(value = "system_country", autoResultMap = true)
@KeySequence("system_country_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCountryDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 中文名称
     */
    private String zhName;

    /**
     * 各个国家的语言名字
     */
    private String countryName;

    /**
     * 英文名称
     */
    private String enName;

    /**
     * 区号
     */
    private String areaCode;

    /**
     * 国家唯一标识
     */
    private String countryCode;

    /**
     * 语言
     */
    private String lang;

    /**
     * 语言中文名称
     */
    private String langZh;

    /**
     * 当地币种
     */
    private String localCurrency;

    /**
     * 对平台币汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 支持的语言 代码1,名称1;代码2,名称2
     */
    private String supportLang;

    /**
     * 当地时区
     */
    private String timeZone;

    /**
     * 商户code值，默认为0
     */
    private String merchantCode;

    /**
     * 是否禁用，0否1是
     */
    private Boolean isFrozen;

    /**
     * 排序号
     */
    private Integer sort;

}
