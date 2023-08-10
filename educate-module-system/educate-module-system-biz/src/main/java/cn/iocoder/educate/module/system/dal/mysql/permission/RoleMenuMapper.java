package cn.iocoder.educate.module.system.dal.mysql.permission;

import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleMenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.UserRoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

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

    default List<RoleMenuDO> selectListByRoleId(Long roleId){
        LambdaQueryWrapper<RoleMenuDO> roleMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuDOLambdaQueryWrapper.eq(RoleMenuDO::getRoleId,roleId);
        return this.selectList(roleMenuDOLambdaQueryWrapper);
    }

    default void deleteListByRoleId(Long roleId){
        LambdaQueryWrapper<RoleMenuDO> roleMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuDOLambdaQueryWrapper.eq(RoleMenuDO::getRoleId,roleId);
        this.delete(roleMenuDOLambdaQueryWrapper);
    }

    default void insertBatch(Collection<RoleMenuDO> entities){
        Db.saveBatch(entities);
    }

    default void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds){
        LambdaQueryWrapper<RoleMenuDO> roleMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuDOLambdaQueryWrapper.eq(RoleMenuDO::getRoleId,roleId)
        // 等价，数组的一个相等元素
                        .in(RoleMenuDO::getMenuId,deleteMenuIds);
        this.delete(roleMenuDOLambdaQueryWrapper);
    }

}
