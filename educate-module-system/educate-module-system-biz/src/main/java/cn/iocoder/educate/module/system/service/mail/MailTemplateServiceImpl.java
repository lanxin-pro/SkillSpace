package cn.iocoder.educate.module.system.service.mail;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplatePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplateUpdateReqVO;
import cn.iocoder.educate.module.system.convert.mail.MailTemplateConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.mail.MailTemplateMapper;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Resource
    private MailProducer mailProducer;

    /**
     * 邮件模板缓存
     * key：邮件模版标识 {@link MailTemplateDO#getCode()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<String, MailTemplateDO> mailTemplateCache;

    /**
     * 正则表达式，匹配 {} 中的变量
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<MailTemplateDO> templates = mailTemplateMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存邮件模版，数量:{}]", templates.size());

        // 第二步：构建缓存
        mailTemplateCache = templates.stream()
                .collect(Collectors.toMap( MailTemplateDO::getCode, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Long createMailTemplate(MailTemplateCreateReqVO mailTemplateCreateReqVO) {
        // 校验 code 是否唯一
        validateCodeUnique(null, mailTemplateCreateReqVO.getCode());

        // 插入
        MailTemplateDO mailTemplateDO = MailTemplateConvert.INSTANCE.convert(mailTemplateCreateReqVO);
        List<String> parseTemplateContentParams = parseTemplateContentParams(mailTemplateCreateReqVO.getContent());
        mailTemplateDO.setParams(parseTemplateContentParams);
        mailTemplateMapper.insert(mailTemplateDO);
        // 发送刷新消息
        mailProducer.sendMailTemplateRefreshMessage();
        return mailTemplateDO.getId();
    }

    @Override
    public void updateMailTemplate(MailTemplateUpdateReqVO mailTemplateUpdateReqVO) {
        // 校验是否存在
        validateMailTemplateExists(mailTemplateUpdateReqVO.getId());
        // 校验 code 是否唯一
        validateCodeUnique(mailTemplateUpdateReqVO.getId(),mailTemplateUpdateReqVO.getCode());

        // 更新
        MailTemplateDO mailTemplateDO = MailTemplateConvert.INSTANCE.convert(mailTemplateUpdateReqVO);
        List<String> parseTemplateContentParams = parseTemplateContentParams(mailTemplateUpdateReqVO.getContent());
        mailTemplateDO.setParams(parseTemplateContentParams);
        mailTemplateMapper.updateById(mailTemplateDO);
        // 发送刷新消息
        mailProducer.sendMailTemplateRefreshMessage();
    }

    @Override
    public void deleteMailTemplate(Long id) {
        // 校验是否存在
        validateMailTemplateExists(id);

        // 删除
        mailTemplateMapper.deleteById(id);
        // 发送刷新消息
        mailProducer.sendMailTemplateRefreshMessage();
    }

    @Override
    public MailTemplateDO getMailTemplate(Long id) {
        return mailTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<MailTemplateDO> getMailTemplatePage(MailTemplatePageReqVO mailTemplatePageReqVO) {
        return mailTemplateMapper.selectPage(mailTemplatePageReqVO);
    }

    @Override
    public List<MailTemplateDO> getMailTemplateList() {
        return mailTemplateMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public MailTemplateDO getMailTemplateByCodeFromCache(String code) {
        return mailTemplateCache.get(code);
    }

    @Override
    public Long countByAccountId(Long accountId) {
        return mailTemplateMapper.selectCountByAccountId(accountId);
    }

    @Override
    public String formatMailTemplateContent(String content, Map<String, Object> params) {
        return StrUtil.format(content, params);
    }

    public void validateCodeUnique(Long id, String code) {
        MailTemplateDO mailTemplateDO = mailTemplateMapper.selectByCode(code);
        if (mailTemplateDO == null) {
            return;
        }
        // 存在 template 记录的情况下
        // 新增时，说明重复
        if (id == null
                // 更新时，如果 id 不一致，说明重复
                || ObjUtil.notEqual(id, mailTemplateDO.getId())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_TEMPLATE_CODE_EXISTS);
        }
    }

    public List<String> parseTemplateContentParams(String content) {
        // 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

    private void validateMailTemplateExists(Long id) {
        if (mailTemplateMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MAIL_TEMPLATE_NOT_EXISTS);
        }
    }

}
