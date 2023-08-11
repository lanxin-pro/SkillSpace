package cn.iocoder.educate.framework.sms.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信模板的审核状态枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/8/11 17:10
 */
@AllArgsConstructor
@Getter
public enum SmsTemplateAuditStatusEnum {

    // 检查
    CHECKING(1),
    // 成功
    SUCCESS(2),
    // 失败
    FAIL(3);

    private final Integer status;

}
