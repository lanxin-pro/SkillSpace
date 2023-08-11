package cn.iocoder.educate.module.system.dal.mysql.sms;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.template.SmsTemplatePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/27 20:32
 */
@Mapper
public interface SmsTemplateMapper extends BaseMapper<SmsTemplateDO> {

    default Long selectCountByChannelId(Long channelId){
        LambdaQueryWrapper<SmsTemplateDO> smsTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        smsTemplateDOLambdaQueryWrapper.eq(SmsTemplateDO::getChannelId,channelId);
        return this.selectCount(smsTemplateDOLambdaQueryWrapper);
    }

    default SmsTemplateDO selectByCode(String code){
        LambdaQueryWrapper<SmsTemplateDO> smsTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        smsTemplateDOLambdaQueryWrapper.eq(SmsTemplateDO::getCode,code);
        return this.selectOne(smsTemplateDOLambdaQueryWrapper);
    }

    default PageResult<SmsTemplateDO> selectPage(SmsTemplatePageReqVO smsTemplatePageReqVO) {
        LambdaQueryWrapper<SmsTemplateDO> smsTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        smsTemplateDOLambdaQueryWrapper
                .like(StringUtils.hasText(smsTemplatePageReqVO.getCode()),
                        SmsTemplateDO::getCode,smsTemplatePageReqVO.getCode())
                .like(StringUtils.hasText(smsTemplatePageReqVO.getContent()),
                        SmsTemplateDO::getContent,smsTemplatePageReqVO.getContent())
                .like(StringUtils.hasText(smsTemplatePageReqVO.getApiTemplateId()),
                        SmsTemplateDO::getApiTemplateId,smsTemplatePageReqVO.getApiTemplateId())
                .eq(ObjectUtil.isNotEmpty(smsTemplatePageReqVO.getType()),
                        SmsTemplateDO::getType,smsTemplatePageReqVO.getType())
                .eq(ObjectUtil.isNotEmpty(smsTemplatePageReqVO.getStatus()),
                        SmsTemplateDO::getStatus,smsTemplatePageReqVO.getStatus())
                .eq(ObjectUtil.isNotEmpty(smsTemplatePageReqVO.getChannelId()),
                        SmsTemplateDO::getChannelId,smsTemplatePageReqVO.getChannelId())
                .between(null != ArrayUtils.get(smsTemplatePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(smsTemplatePageReqVO.getCreateTime(),1) != null,
                        SmsTemplateDO::getCreateTime,
                        ArrayUtils.get(smsTemplatePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(smsTemplatePageReqVO.getCreateTime(),1))
                .orderByDesc(SmsTemplateDO::getId);
        Page<SmsTemplateDO> page = new Page<>(smsTemplatePageReqVO.getPageNo(), smsTemplatePageReqVO.getPageSize());
        Page<SmsTemplateDO> smsTemplateDOPage = this.selectPage(page, smsTemplateDOLambdaQueryWrapper);
        return new PageResult<>(smsTemplateDOPage.getRecords(),smsTemplateDOPage.getTotal());
    }

}
