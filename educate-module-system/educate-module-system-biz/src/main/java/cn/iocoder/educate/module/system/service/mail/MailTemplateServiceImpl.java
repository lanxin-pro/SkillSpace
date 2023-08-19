package cn.iocoder.educate.module.system.service.mail;

import cn.iocoder.educate.module.system.dal.mysql.mail.MailTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 邮箱模版 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/19 9:59
 */
@Service
@Validated
@Slf4j
public class MailTemplateServiceImpl implements MailTemplateService {

    @Resource
    private MailTemplateMapper mailTemplateMapper;

    @Override
    public Long countByAccountId(Long accountId) {
        return mailTemplateMapper.selectCountByAccountId(accountId);
    }

}
