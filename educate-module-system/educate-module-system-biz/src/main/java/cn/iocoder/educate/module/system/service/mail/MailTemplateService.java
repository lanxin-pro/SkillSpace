package cn.iocoder.educate.module.system.service.mail;

/**
 * 邮件模版 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 9:59
 */
public interface MailTemplateService {

    /**
     * 获得指定邮件账号下的邮件模板数量
     *
     * @param id 账号编号
     * @return 数量
     */
    Long countByAccountId(Long id);

}
