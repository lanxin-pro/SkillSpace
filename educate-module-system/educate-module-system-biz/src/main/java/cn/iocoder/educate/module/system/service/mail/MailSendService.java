package cn.iocoder.educate.module.system.service.mail;

import cn.iocoder.educate.module.system.mq.message.mail.MailSendMessage;

import java.util.Map;

/**
 * 邮件发送 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 14:26
 */
public interface MailSendService {

    /**
     * 发送单条邮件给管理后台的用户
     *
     * @param mail 邮箱
     * @param userId 用户编码
     * @param templateCode 邮件模版编码
     * @param templateParams 邮件模版参数
     * @return 发送日志编号
     */
    Long sendSingleMailToAdmin(String mail, Long userId,
                               String templateCode, Map<String, Object> templateParams);

    /**
     * 执行真正的邮件发送
     * 注意，该方法仅仅提供给 MQ Consumer 使用
     *
     * @param message 邮件
     */
    void doSendMail(MailSendMessage message);

}
