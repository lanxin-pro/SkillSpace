package cn.iocoder.educate.module.infra.dal.mysql.logger;

import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:24
 */
@Mapper
public interface ApiAccessLogMapper extends BaseMapper<ApiAccessLogDO> {
}
