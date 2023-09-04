package cn.iocoder.educate.module.mp.dal.mysql.tag;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.tag.vo.MpTagPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.tag.MpTagDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/4 14:11
 */
@Mapper
public interface MpTagMapper extends BaseMapper<MpTagDO> {

    default PageResult<MpTagDO> selectPage(MpTagPageReqVO mpTagPageReqVO) {
        LambdaQueryWrapper<MpTagDO> mpTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpTagDOLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(mpTagPageReqVO.getAccountId()),
                MpTagDO::getAccountId,mpTagPageReqVO.getAccountId())
                .like(StringUtils.hasText(mpTagPageReqVO.getName()),
                        MpTagDO::getName,mpTagPageReqVO.getName())
                .orderByDesc(MpTagDO::getId);
        Page<MpTagDO> page = new Page<>(mpTagPageReqVO.getPageNo(), mpTagPageReqVO.getPageSize());
        Page<MpTagDO> mpAccountDOPage = this.selectPage(page, mpTagDOLambdaQueryWrapper);
        return new PageResult(mpAccountDOPage.getRecords(),mpAccountDOPage.getTotal());
    }

    default List<MpTagDO> selectListByAccountId(Long accountId) {
        LambdaQueryWrapper<MpTagDO> mpTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpTagDOLambdaQueryWrapper.eq(MpTagDO::getAccountId,accountId);
        return this.selectList(mpTagDOLambdaQueryWrapper);
    }

}
