package cn.iocoder.educate.module.system.dal.mysql.mail;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.MailAccountPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/18 13:40
 */
@Mapper
public interface MailAccountMapper extends BaseMapper<MailAccountDO> {

    default PageResult<MailAccountDO> selectPage(MailAccountPageReqVO mailAccountPageReqVO) {
        LambdaQueryWrapper<MailAccountDO> mailAccountDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailAccountDOLambdaQueryWrapper
                .like(StringUtils.hasText(mailAccountPageReqVO.getMail()),
                    MailAccountDO::getMail,mailAccountPageReqVO.getMail())

                .like(StringUtils.hasText(mailAccountPageReqVO.getUsername()),
                        MailAccountDO::getUsername,mailAccountPageReqVO.getUsername());
        Page<MailAccountDO> page = new Page<>(mailAccountPageReqVO.getPageNo(), mailAccountPageReqVO.getPageSize());
        Page<MailAccountDO> mailAccountDOPage = this.selectPage(page, mailAccountDOLambdaQueryWrapper);
        return new PageResult<>(mailAccountDOPage.getRecords(),mailAccountDOPage.getTotal());
    }


}
