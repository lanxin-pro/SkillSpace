package cn.iocoder.educate.module.system.dal.mysql.mail;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.log.MailLogPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 15:03
 */
@Mapper
public interface MailLogMapper extends BaseMapper<MailLogDO> {

    default PageResult<MailLogDO> selectPage(MailLogPageReqVO mailLogPageReqVO) {
        LambdaQueryWrapper<MailLogDO> mailTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mailTemplateDOLambdaQueryWrapper
                .like(StringUtils.hasText(mailLogPageReqVO.getToMail()),
                        MailLogDO::getToMail,mailLogPageReqVO.getToMail())
                .eq(ObjectUtil.isNotEmpty(mailLogPageReqVO.getUserId()),
                        MailLogDO::getUserId,mailLogPageReqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(mailLogPageReqVO.getUserType()),
                        MailLogDO::getUserType,mailLogPageReqVO.getUserType())
                .eq(ObjectUtil.isNotEmpty(mailLogPageReqVO.getAccountId()),
                        MailLogDO::getAccountId,mailLogPageReqVO.getAccountId())
                .eq(ObjectUtil.isNotEmpty(mailLogPageReqVO.getTemplateId()),
                        MailLogDO::getTemplateId,mailLogPageReqVO.getTemplateId())
                .eq(ObjectUtil.isNotEmpty(mailLogPageReqVO.getSendStatus()),
                        MailLogDO::getSendStatus,mailLogPageReqVO.getSendStatus())
                .between(null != ArrayUtils.get(mailLogPageReqVO.getSendTime(),0) &&
                                ArrayUtils.get(mailLogPageReqVO.getSendTime(),1) != null,
                        MailLogDO::getCreateTime,
                        ArrayUtils.get(mailLogPageReqVO.getSendTime(),0),
                        ArrayUtils.get(mailLogPageReqVO.getSendTime(),1))
                .orderByDesc(MailLogDO::getId);
        Page<MailLogDO> page = new Page<>(mailLogPageReqVO.getPageNo(), mailLogPageReqVO.getPageSize());
        Page<MailLogDO> mailTemplateDOPage = this.selectPage(page, mailTemplateDOLambdaQueryWrapper);
        return new PageResult<>(mailTemplateDOPage.getRecords(),mailTemplateDOPage.getTotal());
    }

}
