package cn.iocoder.educate.module.infra.dal.mysql.logger;

import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:09
 * API 错误日志 Mapper
 */
@Mapper
public interface ApiErrorLogMapper extends BaseMapper<ApiErrorLogDO> {
}
