package cn.iocoder.educate.module.system.dal.mysql.notify;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/14 12:32
 */
@Mapper
public interface NotifyMessageMapper extends BaseMapper<NotifyMessageDO> {

    default PageResult<NotifyMessageDO> selectPage(NotifyMessagePageReqVO notifyMessagePageReqVO) {
        LambdaQueryWrapper<NotifyMessageDO> notifyTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyTemplateDOLambdaQueryWrapper
                .eq(ObjectUtil.isNotEmpty(notifyMessagePageReqVO.getTemplateType()),NotifyMessageDO::getTemplateType,
                        notifyMessagePageReqVO.getTemplateType())
                .eq(ObjectUtil.isNotEmpty(notifyMessagePageReqVO.getUserId()),NotifyMessageDO::getUserId,
                        notifyMessagePageReqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(notifyMessagePageReqVO.getUserType()),NotifyMessageDO::getUserType,
                        notifyMessagePageReqVO.getUserType())
                .like(StringUtils.hasText(notifyMessagePageReqVO.getTemplateCode()),NotifyMessageDO::getTemplateCode,
                        notifyMessagePageReqVO.getTemplateCode())
                .between(ArrayUtils.get(notifyMessagePageReqVO.getCreateTime(),0) != null
                                && ArrayUtils.get(notifyMessagePageReqVO.getCreateTime(),1) != null,
                        NotifyMessageDO::getCreateTime,
                        ArrayUtils.get(notifyMessagePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(notifyMessagePageReqVO.getCreateTime(),1))
                .orderByDesc(NotifyMessageDO::getId);
        Page<NotifyMessageDO> page = new Page<>(notifyMessagePageReqVO.getPageNo(),notifyMessagePageReqVO.getPageSize());
        Page<NotifyMessageDO> notifyMessageDOPage = this.selectPage(page, notifyTemplateDOLambdaQueryWrapper);
        return new PageResult<>(notifyMessageDOPage.getRecords(),notifyMessageDOPage.getTotal());
    }

    default List<NotifyMessageDO> selectUnreadListByUserIdAndUserType(Long userId, Integer userType, Integer size){
        LambdaQueryWrapper<NotifyMessageDO> notifyMessageDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyMessageDOLambdaQueryWrapper.eq(NotifyMessageDO::getUserId,userId)
                .eq(NotifyMessageDO::getUserType,userType)
                .eq(NotifyMessageDO::getReadStatus,false)
                .orderByDesc(NotifyMessageDO::getId)
                .last("LIMIT " + size);
        return this.selectList(notifyMessageDOLambdaQueryWrapper);

    }

    default Long selectUnreadCountByUserIdAndUserType(Long userId, Integer userType){
        LambdaQueryWrapper<NotifyMessageDO> notifyMessageDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyMessageDOLambdaQueryWrapper.eq(NotifyMessageDO::getReadStatus, false)
                .eq(NotifyMessageDO::getUserId, userId)
                .eq(NotifyMessageDO::getUserType, userType);
        return this.selectCount(notifyMessageDOLambdaQueryWrapper);
    }

    default PageResult<NotifyMessageDO> selectPage(NotifyMessageMyPageReqVO notifyMessageMyPageReqVO,
                                                   Long userId, Integer userType) {
        LambdaQueryWrapper<NotifyMessageDO> notifyMessageDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyMessageDOLambdaQueryWrapper.eq(NotifyMessageDO::getReadStatus, false)
                .eq(NotifyMessageDO::getUserId, userId)
                .eq(NotifyMessageDO::getUserType, userType)
                .between(ArrayUtils.get(notifyMessageMyPageReqVO.getCreateTime(),0) != null
                                && ArrayUtils.get(notifyMessageMyPageReqVO.getCreateTime(),1) != null,
                        NotifyMessageDO::getCreateTime,
                        ArrayUtils.get(notifyMessageMyPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(notifyMessageMyPageReqVO.getCreateTime(),1))
                .orderByDesc(NotifyMessageDO::getId);
        Page<NotifyMessageDO> page = new Page<>(notifyMessageMyPageReqVO.getPageNo(),notifyMessageMyPageReqVO.getPageSize());
        Page<NotifyMessageDO> notifyMessageDOPage = this.selectPage(page, notifyMessageDOLambdaQueryWrapper);
        return new PageResult<>(notifyMessageDOPage.getRecords(),notifyMessageDOPage.getTotal());
    }

}
