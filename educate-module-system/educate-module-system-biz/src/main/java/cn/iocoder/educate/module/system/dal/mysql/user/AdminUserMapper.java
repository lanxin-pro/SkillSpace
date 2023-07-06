package cn.iocoder.educate.module.system.dal.mysql.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import java.util.Collection;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 20:45
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {

    default PageResult<AdminUserDO> selectPage(UserPageReqVO userPageReqVO, Collection<Long> deptIds){
        LambdaQueryWrapper<AdminUserDO> adminUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminUserDOLambdaQueryWrapper
                .like(StringUtils.hasText(userPageReqVO.getUsername()),AdminUserDO::getUsername,userPageReqVO.getUsername())
                .like(StringUtils.hasText(userPageReqVO.getMobile()),AdminUserDO::getMobile,userPageReqVO.getMobile())
                .eq(ObjectUtil.isNotEmpty(userPageReqVO.getStatus()),AdminUserDO::getStatus,userPageReqVO.getStatus())
                .between(null != ArrayUtils.get(userPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(userPageReqVO.getCreateTime(),1) != null,
                        AdminUserDO::getCreateTime, ArrayUtils.get(userPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(userPageReqVO.getCreateTime(),1))
                .ge(ArrayUtils.get(userPageReqVO.getCreateTime(),0) !=
                        null,
                        AdminUserDO::getCreateTime,
                        ArrayUtils.get(userPageReqVO.getCreateTime(),0))
                .le(ArrayUtils.get(userPageReqVO.getCreateTime(),1) !=
                        null,
                        AdminUserDO::getCreateTime,
                        ArrayUtils.get(userPageReqVO.getCreateTime(),1))
                .in(CollectionUtil.isNotEmpty(deptIds),AdminUserDO::getDeptId,deptIds)
                .orderByDesc(AdminUserDO::getId);
        Page<AdminUserDO> mpPage = new Page<>(userPageReqVO.getPageNo(), userPageReqVO.getPageSize());
        Page<AdminUserDO> adminUserDOPage = this.selectPage(mpPage, adminUserDOLambdaQueryWrapper);
        return new PageResult<>(adminUserDOPage.getRecords(),adminUserDOPage.getTotal());
    }



}
