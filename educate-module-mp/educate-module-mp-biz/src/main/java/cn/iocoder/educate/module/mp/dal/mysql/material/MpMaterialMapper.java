package cn.iocoder.educate.module.mp.dal.mysql.material;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/11 14:53
 */
@Mapper
public interface MpMaterialMapper extends BaseMapper<MpMaterialDO> {

    default PageResult<MpMaterialDO> selectPage(MpMaterialPageReqVO mpMaterialPageReqVO) {
        LambdaQueryWrapper<MpMaterialDO> mpMaterialDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpMaterialDOLambdaQueryWrapper
                .like(MpMaterialDO::getAccountId,mpMaterialPageReqVO.getAccountId())
                .eq(ObjectUtil.isNotEmpty(mpMaterialPageReqVO.getPermanent()),
                        MpMaterialDO::getPermanent,mpMaterialPageReqVO.getPermanent())
                .eq(ObjectUtil.isNotEmpty(mpMaterialPageReqVO.getType()),
                        MpMaterialDO::getType,mpMaterialPageReqVO.getType())
                .orderByDesc(MpMaterialDO::getId);
        Page<MpMaterialDO> page = new Page<>(mpMaterialPageReqVO.getPageNo(), mpMaterialPageReqVO.getPageSize());
        Page<MpMaterialDO> mpAccountDOPage = this.selectPage(page, mpMaterialDOLambdaQueryWrapper);
        return new PageResult(mpAccountDOPage.getRecords(),mpAccountDOPage.getTotal());
    }

    default List<MpMaterialDO> selectListByMediaId(Collection<String> mediaIds) {
        LambdaQueryWrapper<MpMaterialDO> mpMaterialDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpMaterialDOLambdaQueryWrapper.in(MpMaterialDO::getMediaId, mediaIds);
        return selectList(mpMaterialDOLambdaQueryWrapper);
    }

}
