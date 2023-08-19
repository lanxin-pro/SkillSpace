package cn.iocoder.educate.module.system.service.mail;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.convert.mail.MailAccountConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.message.mail.MailSendMessage;
import cn.iocoder.educate.module.system.mq.producer.mail.MailProducer;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 邮箱发送 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 14:26
 */
@Service
@Validated
@Slf4j
public class MailSendServiceImpl implements MailSendService {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private MailTemplateService mailTemplateService;

    @Resource
    private MailAccountService mailAccountService;

    @Resource
    private MailProducer mailProducer;

    @Resource
    private MailLogService mailLogService;

    @Override
    public Long sendSingleMailToAdmin(String mail, Long userId, String templateCode, Map<String, Object> templateParams) {
        // 执行发送
        return sendSingleMail(mail, userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    /**
     * 注意，该方法仅仅提供给 MQ Consumer 使用
     *
     * 参考文档地址：https://doc.hutool.cn/pages/MailUtil/
     *
     * @param message 邮件
     */
    @Override
    public void doSendMail(MailSendMessage message) {
        // 1. 创建发送账号
        MailAccountDO account = validateMailAccount(message.getAccountId());
        // 发送人
        MailAccount mailAccount  = MailAccountConvert.INSTANCE.convert(account, message.getNickname());
        // 2. 发送邮件
        try {
            String messageId = MailUtil.send(mailAccount, message.getMail(),
                    message.getTitle(), message.getContent(),true);
            // 3. 更新结果（成功）
            mailLogService.updateMailSendResult(message.getLogId(), messageId, null);
        } catch (Exception e) {
            // 3. 更新结果（异常）
            mailLogService.updateMailSendResult(message.getLogId(), null, e);
        }
    }

    public Long sendSingleMail(String mail, Long userId, Integer userType,
                               String templateCode, Map<String, Object> templateParams) {
        // 如果 mail 为空，则加载用户编号对应的邮箱
        if (StrUtil.isEmpty(mail)) {
            AdminUserDO user = adminUserService.getUser(userId);
            if (user != null) {
                mail = user.getEmail();
            }
        }
        // 校验邮箱模版是否合法
        MailTemplateDO template = validateMailTemplate(templateCode);
        // 校验邮箱账号是否合法
        MailAccountDO account = validateMailAccount(template.getAccountId());

        // 校验邮箱是否存在
        mail = validateMail(mail);
        validateTemplateParams(template, templateParams);
        // 创建发送日志。如果模板被禁用，则不发送短信，只记录日志
        Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus());
        String title = mailTemplateService.formatMailTemplateContent(template.getTitle(), templateParams);
        String content = mailTemplateService.formatMailTemplateContent(template.getContent(), templateParams);
        Long sendLogId = mailLogService.createMailLog(userId, userType, mail,
                account, template, content, templateParams, isSend);
        // 发送 MQ 消息，异步执行发送短信
        if (isSend) {
            mailProducer.sendMailSendMessage(sendLogId, mail, account.getId(),
                    template.getNickname(), title, content);
        }
        return sendLogId;
    }

    MailTemplateDO validateMailTemplate(String templateCode) {
        // 获得邮件模板。考虑到效率，从缓存中获取
        MailTemplateDO mailTemplateDO = mailTemplateService.getMailTemplateByCodeFromCache(templateCode);
        // 邮件模板不存在
        if (mailTemplateDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_TEMPLATE_NOT_EXISTS);
        }
        return mailTemplateDO;
    }

    MailAccountDO validateMailAccount(Long accountId) {
        // 获得邮箱账号。考虑到效率，从缓存中获取
        MailAccountDO account = mailAccountService.getMailAccountFromCache(accountId);
        // 邮箱账号不存在
        if (account == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_ACCOUNT_NOT_EXISTS);
        }
        return account;
    }

    String validateMail(String mail) {
        if (StrUtil.isEmpty(mail)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_SEND_MAIL_NOT_EXISTS);
        }
        return mail;
    }

    /**
     * 校验邮件参数是否确实
     *
     * @param template 邮箱模板
     * @param templateParams 参数列表
     */
    void validateTemplateParams(MailTemplateDO template, Map<String, Object> templateParams) {
        template.getParams().forEach(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_SEND_TEMPLATE_PARAM_MISS, key);
            }
        });
    }

}
