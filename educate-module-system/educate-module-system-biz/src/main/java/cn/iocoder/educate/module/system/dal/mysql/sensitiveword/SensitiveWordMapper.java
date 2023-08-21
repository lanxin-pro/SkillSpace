package cn.iocoder.educate.module.system.dal.mysql.sensitiveword;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sensitiveword.SensitiveWordDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/21 8:18
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWordDO> {

    default SensitiveWordDO selectByName(String name){
        LambdaQueryWrapper<SensitiveWordDO> sensitiveWordDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sensitiveWordDOLambdaQueryWrapper.eq(SensitiveWordDO::getName,name);
        return this.selectOne(sensitiveWordDOLambdaQueryWrapper);
    }

    default PageResult<SensitiveWordDO> selectPage(SensitiveWordPageReqVO sensitiveWordPageReqVO) {
        LambdaQueryWrapper<SensitiveWordDO> sensitiveWordDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sensitiveWordDOLambdaQueryWrapper
                .like(StringUtils.hasText(sensitiveWordPageReqVO.getName()),
                        SensitiveWordDO::getName,sensitiveWordPageReqVO.getName())
                .like(StringUtils.hasText(sensitiveWordPageReqVO.getTag()),
                        SensitiveWordDO::getTags,sensitiveWordPageReqVO.getTag())
                .eq(ObjUtil.isNotEmpty(sensitiveWordPageReqVO.getStatus()),
                        SensitiveWordDO::getStatus,sensitiveWordPageReqVO.getStatus())
                .between(null != ArrayUtils.get(sensitiveWordPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(sensitiveWordPageReqVO.getCreateTime(),1) != null,
                        SensitiveWordDO::getCreateTime,
                        ArrayUtils.get(sensitiveWordPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(sensitiveWordPageReqVO.getCreateTime(),1))
                .orderByDesc(SensitiveWordDO::getId);
        Page<SensitiveWordDO> page = new Page<>(sensitiveWordPageReqVO.getPageNo(), sensitiveWordPageReqVO.getPageSize());
        Page<SensitiveWordDO> sensitiveWordDOPage = this.selectPage(page,sensitiveWordDOLambdaQueryWrapper);
        return new PageResult<>(sensitiveWordDOPage.getRecords(),sensitiveWordDOPage.getTotal());
    }

    default void insertBatch(List<SensitiveWordDO> sensitiveWordDOS){
        Db.saveBatch(sensitiveWordDOS);
    }

}
