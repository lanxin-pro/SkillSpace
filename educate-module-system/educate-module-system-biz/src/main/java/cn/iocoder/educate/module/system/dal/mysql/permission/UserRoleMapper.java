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
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    default List<UserRoleDO> selectListByUserId(Long userId){
        LambdaQueryWrapper<UserRoleDO> userRoleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleDOLambdaQueryWrapper.eq(UserRoleDO::getUserId,userId);
        return this.selectList(userRoleDOLambdaQueryWrapper);
    }

    default void deleteListByUserId(Long userId){
        LambdaQueryWrapper<UserRoleDO> userRoleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleDOLambdaQueryWrapper.eq(UserRoleDO::getUserId,userId);
        this.delete(userRoleDOLambdaQueryWrapper);
    }

    default void deleteListByRoleId(Long roleId){
        LambdaQueryWrapper<UserRoleDO> userRoleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleDOLambdaQueryWrapper.eq(UserRoleDO::getRoleId,roleId);
        this.delete(userRoleDOLambdaQueryWrapper);
    }

    default void insertBatch(List<UserRoleDO> userRoleDOSList){
        Db.saveBatch(userRoleDOSList);
    }

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> deleteMenuIds){
        LambdaQueryWrapper<UserRoleDO> userRoleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleDOLambdaQueryWrapper.eq(UserRoleDO::getUserId,userId)
                .in(UserRoleDO::getRoleId,deleteMenuIds);
        this.delete(userRoleDOLambdaQueryWrapper);
    }

}
