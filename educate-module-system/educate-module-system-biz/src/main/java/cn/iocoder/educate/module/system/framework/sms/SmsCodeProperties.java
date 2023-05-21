package cn.iocoder.educate.module.system.framework.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/20 20:31
 */
@ConfigurationProperties(prefix = "lanxin.sms-code")
@Validated
@Data
public class SmsCodeProperties {

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    private Duration expireTimes;

    /**
     * 短信发送频率
     */
    @NotNull(message = "短信发送频率不能为空")
    private Duration sendFrequency;

    /**
     * 每日发送最大数量
     */
    @NotNull(message = "每日发送最大数量不能为空")
    private Integer sendMaximumQuantityPerDay;

    /**
     * 每小时发送的ip数量
     */
    @NotNull(message = "每小时发送IP最大数量不能为空")
    private Integer sendMaximumQuantityPerIp;

    /**
     * 验证码最小值
     */
    @NotNull(message = "验证码最小值不能为空")
    private Integer beginCode;

    /**
     * 验证码最大值
     */
    @NotNull(message = "验证码最大值不能为空")
    private Integer endCode;
}
