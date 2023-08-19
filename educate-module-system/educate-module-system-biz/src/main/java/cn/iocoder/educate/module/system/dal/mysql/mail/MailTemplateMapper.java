package cn.iocoder.educate.module.system.dal.mysql.mail;

import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 10:01
 */
@Mapper
public interface MailTemplateMapper extends BaseMapper<MailTemplateDO> {

    default Long selectCountByAccountId(Long accountId) {
        LambdaQueryWrapper<MailTemplateDO> mailTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailTemplateDOLambdaQueryWrapper.eq(MailTemplateDO::getAccountId,accountId);
        return this.selectCount(mailTemplateDOLambdaQueryWrapper);
    }

}
