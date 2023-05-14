package cn.iocoder.educate.module.system.dal.mysql.logger;

import cn.iocoder.educate.module.system.dal.dataobject.logger.LoginLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/12 21:20
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogDO> {
}
