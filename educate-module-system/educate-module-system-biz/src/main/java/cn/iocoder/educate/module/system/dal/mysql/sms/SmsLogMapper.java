package cn.iocoder.educate.module.system.dal.mysql.sms;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.log.SmsLogPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/30 10:29
 */
@Mapper
public interface SmsLogMapper extends BaseMapper<SmsLogDO> {

    default PageResult<SmsLogDO> selectPage(SmsLogPageReqVO smsLogPageReqVO) {
        LambdaQueryWrapper<SmsLogDO> smsTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        smsTemplateDOLambdaQueryWrapper
                .like(StringUtils.hasText(smsLogPageReqVO.getMobile()),
                        SmsLogDO::getMobile,smsLogPageReqVO.getMobile())
                .eq(ObjectUtil.isNotEmpty(smsLogPageReqVO.getChannelId()),
                        SmsLogDO::getChannelId,smsLogPageReqVO.getChannelId())
                .eq(ObjectUtil.isNotEmpty(smsLogPageReqVO.getTemplateId()),
                        SmsLogDO::getTemplateId,smsLogPageReqVO.getTemplateId())
                .eq(ObjectUtil.isNotEmpty(smsLogPageReqVO.getSendStatus()),
                        SmsLogDO::getSendStatus,smsLogPageReqVO.getSendStatus())
                .eq(ObjectUtil.isNotEmpty(smsLogPageReqVO.getReceiveStatus()),
                        SmsLogDO::getReceiveStatus,smsLogPageReqVO.getReceiveStatus())
                .between(null != ArrayUtils.get(smsLogPageReqVO.getReceiveTime(),0) &&
                                ArrayUtils.get(smsLogPageReqVO.getReceiveTime(),1) != null,
                        SmsLogDO::getReceiveTime,
                        ArrayUtils.get(smsLogPageReqVO.getReceiveTime(),0),
                        ArrayUtils.get(smsLogPageReqVO.getReceiveTime(),1))
                .between(null != ArrayUtils.get(smsLogPageReqVO.getSendTime(),0) &&
                                ArrayUtils.get(smsLogPageReqVO.getSendTime(),1) != null,
                        SmsLogDO::getSendTime,
                        ArrayUtils.get(smsLogPageReqVO.getSendTime(),0),
                        ArrayUtils.get(smsLogPageReqVO.getSendTime(),1))
                .orderByDesc(SmsLogDO::getId);
        Page<SmsLogDO> page = new Page<>(smsLogPageReqVO.getPageNo(), smsLogPageReqVO.getPageSize());
        Page<SmsLogDO> smsLogDOPage = this.selectPage(page, smsTemplateDOLambdaQueryWrapper);
        return new PageResult<>(smsLogDOPage.getRecords(),smsLogDOPage.getTotal());
    }

}
