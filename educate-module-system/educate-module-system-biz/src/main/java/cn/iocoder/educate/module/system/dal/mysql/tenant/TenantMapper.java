package cn.iocoder.educate.module.system.dal.mysql.tenant;

import cn.iocoder.educate.module.system.dal.dataobject.tenant.TenantDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户 Mapper
 *
 * @Author: 董伟豪
 * @Date: 2023/6/6 16:44
 */
@Mapper
public interface TenantMapper extends BaseMapper<TenantDO> {

    default TenantDO selectByName(String name){
        LambdaQueryWrapper<TenantDO> tenantDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tenantDOLambdaQueryWrapper.eq(TenantDO::getName,name);
        return this.selectOne(tenantDOLambdaQueryWrapper);
    }

}
