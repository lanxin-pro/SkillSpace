package cn.iocoder.educate.module.system.dal.mysql.mail;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.MailTemplatePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 10:01
 */
@Mapper
public interface MailTemplateMapper extends BaseMapper<MailTemplateDO> {

    default PageResult<MailTemplateDO> selectPage(MailTemplatePageReqVO mailTemplatePageReqVO){
        LambdaQueryWrapper<MailTemplateDO> mailTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailTemplateDOLambdaQueryWrapper
                .like(StringUtils.hasText(mailTemplatePageReqVO.getCode()),
                        MailTemplateDO::getCode,mailTemplatePageReqVO.getCode())

                .like(StringUtils.hasText(mailTemplatePageReqVO.getName()),
                        MailTemplateDO::getName,mailTemplatePageReqVO.getName())

                .eq(ObjectUtil.isNotEmpty(mailTemplatePageReqVO.getStatus()),
                        MailTemplateDO::getStatus,mailTemplatePageReqVO.getStatus())

                .eq(ObjectUtil.isNotEmpty(mailTemplatePageReqVO.getAccountId()),
                        MailTemplateDO::getAccountId,mailTemplatePageReqVO.getAccountId())

                .between(null != ArrayUtils.get(mailTemplatePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(mailTemplatePageReqVO.getCreateTime(),1) != null,
                        MailTemplateDO::getCreateTime,
                        ArrayUtils.get(mailTemplatePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(mailTemplatePageReqVO.getCreateTime(),1));
        Page<MailTemplateDO> page = new Page<>(mailTemplatePageReqVO.getPageNo(), mailTemplatePageReqVO.getPageSize());
        Page<MailTemplateDO> mailTemplateDOPage = this.selectPage(page, mailTemplateDOLambdaQueryWrapper);
        return new PageResult<>(mailTemplateDOPage.getRecords(),mailTemplateDOPage.getTotal());
    }

    default Long selectCountByAccountId(Long accountId) {
        LambdaQueryWrapper<MailTemplateDO> mailTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailTemplateDOLambdaQueryWrapper.eq(MailTemplateDO::getAccountId,accountId);
        return this.selectCount(mailTemplateDOLambdaQueryWrapper);
    }

    default MailTemplateDO selectByCode(String code) {
        LambdaQueryWrapper<MailTemplateDO> mailTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailTemplateDOLambdaQueryWrapper.eq(MailTemplateDO::getCode,code);
        return this.selectOne(mailTemplateDOLambdaQueryWrapper);
    }

}
