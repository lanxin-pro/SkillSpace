package cn.iocoder.educate.module.system.service.mail;

import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;

import java.util.Map;

/**
 * 邮件日志 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 14:56
 */
public interface MailLogService {

    /**
     * 创建邮件日志
     *
     * @param userId 用户编码
     * @param userType 用户类型
     * @param toMail 收件人邮件
     * @param account 邮件账号信息
     * @param template      模版信息
     * @param templateContent 模版内容
     * @param templateParams 模版参数
     * @param isSend        是否发送成功
     * @return 日志编号
     */
    Long createMailLog(Long userId, Integer userType, String toMail,
                       MailAccountDO account, MailTemplateDO template ,
                       String templateContent, Map<String, Object> templateParams, Boolean isSend);

    /**
     * 更新邮件发送结果
     *
     * @param logId  日志编号
     * @param messageId 发送后的消息编号
     * @param exception 发送异常
     */
    void updateMailSendResult(Long logId, String messageId, Exception exception);


}
