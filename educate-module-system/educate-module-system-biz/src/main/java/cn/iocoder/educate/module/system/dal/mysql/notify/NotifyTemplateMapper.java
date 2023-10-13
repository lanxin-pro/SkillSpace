package cn.iocoder.educate.module.system.dal.mysql.notify;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.template.NotifyTemplatePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/11 20:33
 */
@Mapper
public interface NotifyTemplateMapper extends BaseMapper<NotifyTemplateDO> {

    default PageResult<NotifyTemplateDO> selectPage(NotifyTemplatePageReqVO notifyTemplatePageReqVO) {
        LambdaQueryWrapper<NotifyTemplateDO> notifyTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyTemplateDOLambdaQueryWrapper
                .like(StringUtils.hasText(notifyTemplatePageReqVO.getName()),NotifyTemplateDO::getName,
                        notifyTemplatePageReqVO.getName())
                .like(StringUtils.hasText(notifyTemplatePageReqVO.getCode()),NotifyTemplateDO::getCode,
                        notifyTemplatePageReqVO.getCode())
                .eq(ObjectUtil.isNotEmpty(notifyTemplatePageReqVO.getStatus()),NotifyTemplateDO::getStatus,
                        notifyTemplatePageReqVO.getStatus())
                .between(ArrayUtils.get(notifyTemplatePageReqVO.getCreateTime(),0) != null
                                && ArrayUtils.get(notifyTemplatePageReqVO.getCreateTime(),1) != null,
                        NotifyTemplateDO::getCreateTime,
                        ArrayUtils.get(notifyTemplatePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(notifyTemplatePageReqVO.getCreateTime(),1))
                .orderByDesc(NotifyTemplateDO::getId);
        Page<NotifyTemplateDO> page = new Page<>(notifyTemplatePageReqVO.getPageNo(),notifyTemplatePageReqVO.getPageSize());
        Page<NotifyTemplateDO> roleDOPage = this.selectPage(page, notifyTemplateDOLambdaQueryWrapper);
        return new PageResult<>(roleDOPage.getRecords(),roleDOPage.getTotal());
    }

    default NotifyTemplateDO selectByCode(String code){
        LambdaQueryWrapper<NotifyTemplateDO> notifyTemplateDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        notifyTemplateDOLambdaQueryWrapper.eq(NotifyTemplateDO::getCode,code);
        return this.selectOne(notifyTemplateDOLambdaQueryWrapper);
    }
}
