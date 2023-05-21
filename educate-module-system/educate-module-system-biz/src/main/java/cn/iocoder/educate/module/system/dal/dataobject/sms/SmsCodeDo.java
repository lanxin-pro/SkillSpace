package cn.iocoder.educate.module.system.dal.dataobject.sms;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/19 12:07
 */
@TableName("system_sms_code")
@KeySequence("system_sms_code_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeDo extends BaseDO{

    /**
     * 编号
     */
    private Integer id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 发送场景
     *
     * 枚举 {@link SmsCodeDO}
     */
    private Integer scene;

    /**
     * 创建 IP
     */
    private String createIp;

    /**
     * 今日发送的第几条
     */
    private Integer todayIndex;

    /**
     * ip发送的第几条
     */
    private Integer ipIndex;

    /**
     * 是否使用
     */
    private Boolean used;

    /**
     * 使用时间
     */
    private LocalDateTime usedTime;

    /**
     * 使用 IP
     */
    private String usedIp;

}
