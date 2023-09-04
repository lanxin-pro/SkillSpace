package cn.iocoder.educate.module.mp.dal.mysql.user;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:30
 */
@Mapper
public interface MpUserMapper extends BaseMapper<MpUserDO> {

    default PageResult<MpUserDO> selectPage(MpUserPageReqVO mpUserPageReqVO){
        LambdaQueryWrapper<MpUserDO> mpUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpUserDOLambdaQueryWrapper
                .like(StringUtils.hasText(mpUserPageReqVO.getOpenid()),
                        MpUserDO::getOpenid,mpUserPageReqVO.getOpenid())
                .like(StringUtils.hasText(mpUserPageReqVO.getNickname()),
                        MpUserDO::getNickname,mpUserPageReqVO.getNickname())
                .eq(ObjectUtil.isNotEmpty(mpUserPageReqVO.getAccountId()),
                        MpUserDO::getAccountId,mpUserPageReqVO.getAccountId())
                .orderByDesc(MpUserDO::getId);
        Page<MpUserDO> page = new Page<>(mpUserPageReqVO.getPageNo(), mpUserPageReqVO.getPageSize());
        Page<MpUserDO> mpAutoReplyDOPage = this.selectPage(page, mpUserDOLambdaQueryWrapper);
        return new PageResult(mpAutoReplyDOPage.getRecords(),mpAutoReplyDOPage.getTotal());
    }


    default List<MpUserDO> selectListByAppIdAndOpenid(String appId, List<String> openids) {
        LambdaQueryWrapper<MpUserDO> mpUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpUserDOLambdaQueryWrapper.eq(MpUserDO::getAppId, appId)
                .in(MpUserDO::getOpenid, openids);
        return this.selectList(mpUserDOLambdaQueryWrapper);
    }

    default void insertBatch(List<MpUserDO> newUsers){
        Db.saveBatch(newUsers);
    }
}
