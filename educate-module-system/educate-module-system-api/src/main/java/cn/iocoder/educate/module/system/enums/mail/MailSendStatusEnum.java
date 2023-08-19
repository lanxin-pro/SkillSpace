package cn.iocoder.educate.module.system.enums.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件的发送状态枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 15:01
 */
@Getter
@AllArgsConstructor
public enum MailSendStatusEnum {

    INIT(0), // 初始化
    SUCCESS(10), // 发送成功
    FAILURE(20), // 发送失败
    IGNORE(30), // 忽略，即不发送
    ;

    private final int status;

}
