package cn.iocoder.educate.module.system.dal.mysql.logger;

import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:09
 */
@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLogDO> {
}
