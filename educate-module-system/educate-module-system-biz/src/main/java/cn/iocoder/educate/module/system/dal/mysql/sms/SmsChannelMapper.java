package cn.iocoder.educate.module.system.dal.mysql.sms;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.SmsChannelPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/28 19:14
 */
@Mapper
public interface SmsChannelMapper extends BaseMapper<SmsChannelDO> {

    default PageResult<SmsChannelDO> selectPage(SmsChannelPageReqVO smsChannelPageReqVO) {
        LambdaQueryWrapper<SmsChannelDO> smsChannelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        smsChannelDOLambdaQueryWrapper
                .like(StringUtils.hasText(smsChannelPageReqVO.getSignature()),
                        SmsChannelDO::getSignature,smsChannelPageReqVO.getSignature())
                .eq(ObjUtil.isNotEmpty(smsChannelPageReqVO.getStatus()),
                        SmsChannelDO::getStatus,smsChannelPageReqVO.getStatus())
                .between(null != ArrayUtils.get(smsChannelPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(smsChannelPageReqVO.getCreateTime(),1) != null,
                        SmsChannelDO::getCreateTime,
                        ArrayUtils.get(smsChannelPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(smsChannelPageReqVO.getCreateTime(),1))
                .orderByDesc(SmsChannelDO::getId);
        Page<SmsChannelDO> page = new Page<>(smsChannelPageReqVO.getPageNo(), smsChannelPageReqVO.getPageSize());
        Page<SmsChannelDO> smsChannelDOPage = this.selectPage(page,smsChannelDOLambdaQueryWrapper);
        return new PageResult<>(smsChannelDOPage.getRecords(),smsChannelDOPage.getTotal());
    }

}
