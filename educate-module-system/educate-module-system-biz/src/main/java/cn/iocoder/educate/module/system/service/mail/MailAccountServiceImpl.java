package cn.iocoder.educate.module.system.service.mail;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.MailAccountCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.MailAccountPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.MailAccountUpdateReqVO;
import cn.iocoder.educate.module.system.convert.mail.MailAccountConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.educate.module.system.dal.mysql.mail.MailAccountMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.producer.mail.MailProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 邮箱账号 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/18 13:40
 */
@Service
@Validated
@Slf4j
public class MailAccountServiceImpl implements MailAccountService {

    @Resource
    private MailAccountMapper mailAccountMapper;

    @Resource
    private MailTemplateService mailTemplateService;

    /**
     * 邮箱账号缓存
     * key：邮箱账号编码 {@link MailAccountDO#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<Long, MailAccountDO> mailAccountCache;

    @Resource
    private MailProducer mailProducer;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<MailAccountDO> accounts = mailAccountMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存邮箱账号，数量:{}]", accounts.size());

        // 第二步：构建缓存
        mailAccountCache = accounts.stream()
                .collect(
                        Collectors.toMap(MailAccountDO::getId,
                        Function.identity(),
                        (v1,v2)->v1)
                );
    }

    @Override
    public MailAccountDO getMailAccountFromCache(Long id) {
        return mailAccountCache.get(id);
    }

    @Override
    public Long createMailAccount(MailAccountCreateReqVO mailAccountCreateReqVO) {
        // 插入
        MailAccountDO account = MailAccountConvert.INSTANCE.convert(mailAccountCreateReqVO);
        mailAccountMapper.insert(account);

        // 发送刷新消息
        mailProducer.sendMailAccountRefreshMessage();
        return account.getId();
    }

    @Override
    public void updateMailAccount(MailAccountUpdateReqVO mailAccountUpdateReqVO) {
        // 校验是否存在
        validateMailAccountExists(mailAccountUpdateReqVO.getId());

        // 更新
        MailAccountDO mailAccountDO = MailAccountConvert.INSTANCE.convert(mailAccountUpdateReqVO);
        mailAccountMapper.updateById(mailAccountDO);
        // 发送刷新消息
        mailProducer.sendMailAccountRefreshMessage();
    }

    @Override
    public void deleteMailAccount(Long id) {
        // 校验是否存在账号
        validateMailAccountExists(id);
        // 校验是否存在关联模版
        if (mailTemplateService.countByAccountId(id) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_ACCOUNT_RELATE_TEMPLATE_EXISTS);
        }

        // 删除
        mailAccountMapper.deleteById(id);
        // 发送刷新消息
        mailProducer.sendMailAccountRefreshMessage();
    }

    @Override
    public MailAccountDO getMailAccount(Long id) {
        return mailAccountCache.get(id);
    }

    @Override
    public PageResult<MailAccountDO> getMailAccountPage(MailAccountPageReqVO mailAccountPageReqVO) {
        return mailAccountMapper.selectPage(mailAccountPageReqVO);
    }

    @Override
    public List<MailAccountDO> getMailAccountList() {
        return mailAccountMapper.selectList(new LambdaQueryWrapper<>());
    }

    private void validateMailAccountExists(Long id) {
        if (mailAccountCache.get(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_ACCOUNT_NOT_EXISTS);
        }
    }

}
