package cn.iocoder.educate.module.system.dal.mysql.permission;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/23 15:23
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    default PageResult<RoleDO> selectPage(RolePageReqVO rolePageReqVO) {
        LambdaQueryWrapper<RoleDO> roleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleDOLambdaQueryWrapper
                .eq(StringUtils.hasText(rolePageReqVO.getName()),RoleDO::getName,rolePageReqVO.getName())
                .eq(StringUtils.hasText(rolePageReqVO.getCode()),RoleDO::getCode,rolePageReqVO.getCode())
                .eq(ObjectUtil.isNotEmpty(rolePageReqVO.getStatus()),RoleDO::getStatus,rolePageReqVO.getStatus())
                .between(ArrayUtils.get(rolePageReqVO.getCreateTime(),0) != null
                                && ArrayUtils.get(rolePageReqVO.getCreateTime(),1) != null,
                        RoleDO::getCreateTime,
                        ArrayUtils.get(rolePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(rolePageReqVO.getCreateTime(),1))
                .orderByDesc(RoleDO::getId);
        Page<RoleDO> page = new Page<>(rolePageReqVO.getPageNo(),rolePageReqVO.getPageSize());
        Page<RoleDO> roleDOPage = this.selectPage(page, roleDOLambdaQueryWrapper);
        return new PageResult<>(roleDOPage.getRecords(),roleDOPage.getTotal());
    }

}
