package cn.iocoder.educate.module.system.dal.mysql.permission;

import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleMenuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/23 11:07
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDO> {

    default void deleteListByMenuId(Long menuId){
        LambdaQueryWrapper<RoleMenuDO> roleMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuDOLambdaQueryWrapper.eq(RoleMenuDO::getMenuId,menuId);
        this.delete(roleMenuDOLambdaQueryWrapper);
    }

}
