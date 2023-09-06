package cn.iocoder.educate.module.mp.dal.mysql.message;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/6 11:53
 */
@Mapper
public interface MpMessageMapper extends BaseMapper<MpMessageDO> {

    default PageResult<MpMessageDO> selectPage(MpMessagePageReqVO mpMessagePageReqVO) {
        LambdaQueryWrapper<MpMessageDO> mpMessageDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpMessageDOLambdaQueryWrapper
                .eq(ObjectUtil.isNotEmpty(mpMessagePageReqVO.getAccountId()),
                        MpMessageDO::getAccountId,mpMessagePageReqVO.getAccountId())
                .eq(ObjectUtil.isNotEmpty(mpMessagePageReqVO.getType()),
                        MpMessageDO::getType,mpMessagePageReqVO.getType())
                .like(StringUtils.hasText(mpMessagePageReqVO.getOpenid()),
                        MpMessageDO::getOpenid,mpMessagePageReqVO.getOpenid())
                .between(ArrayUtils.get(mpMessagePageReqVO.getCreateTime(),0) != null
                                && ArrayUtils.get(mpMessagePageReqVO.getCreateTime(),1) != null,
                        MpMessageDO::getCreateTime,
                        ArrayUtils.get(mpMessagePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(mpMessagePageReqVO.getCreateTime(),1))
                .orderByDesc(MpMessageDO::getId);
        Page<MpMessageDO> page = new Page<>(mpMessagePageReqVO.getPageNo(), mpMessagePageReqVO.getPageSize());
        Page<MpMessageDO> mpAutoReplyDOPage = this.selectPage(page, mpMessageDOLambdaQueryWrapper);
        return new PageResult(mpAutoReplyDOPage.getRecords(),mpAutoReplyDOPage.getTotal());
    }

}
