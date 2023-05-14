package cn.iocoder.educate.module.system.dal.mysql.user;

import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 20:45
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {
}
