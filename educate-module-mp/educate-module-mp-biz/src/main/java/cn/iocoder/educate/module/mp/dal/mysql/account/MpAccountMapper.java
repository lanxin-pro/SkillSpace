package cn.iocoder.educate.module.mp.dal.mysql.account;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountRespVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/31 11:42
 */
@Mapper
public interface MpAccountMapper extends BaseMapper<MpAccountDO> {

    default PageResult<MpAccountDO> selectPage(MpAccountPageReqVO mpAccountPageReqVO) {
        LambdaQueryWrapper<MpAccountDO> mpAccountDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpAccountDOLambdaQueryWrapper
                .like(StringUtils.hasText(mpAccountPageReqVO.getName()),
                        MpAccountDO::getName,mpAccountPageReqVO.getName())
                .like(StringUtils.hasText(mpAccountPageReqVO.getAccount()),
                        MpAccountDO::getAccount,mpAccountPageReqVO.getAccount())
                .like(StringUtils.hasText(mpAccountPageReqVO.getAppId()),
                        MpAccountDO::getAppId,mpAccountPageReqVO.getAppId())
                .orderByDesc(MpAccountDO::getId);
        Page<MpAccountDO> page = new Page<>(mpAccountPageReqVO.getPageNo(), mpAccountPageReqVO.getPageSize());
        Page<MpAccountDO> mpAccountDOPage = this.selectPage(page, mpAccountDOLambdaQueryWrapper);
        return new PageResult(mpAccountDOPage.getRecords(),mpAccountDOPage.getTotal());
    }

    default MpAccountDO selectByAppId(String appId) {
        LambdaQueryWrapper<MpAccountDO> mpAccountDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpAccountDOLambdaQueryWrapper.eq(MpAccountDO::getAppId,appId);
        return this.selectOne(mpAccountDOLambdaQueryWrapper);
    }

}
