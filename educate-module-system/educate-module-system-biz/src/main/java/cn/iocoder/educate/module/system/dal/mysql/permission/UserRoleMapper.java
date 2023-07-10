package cn.iocoder.educate.module.system.dal.mysql.permission;

import cn.iocoder.educate.module.system.dal.dataobject.permission.UserRoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
