package cn.iocoder.educate.module.infra.dal.mysql.db;

import cn.iocoder.educate.module.infra.dal.dataobject.db.DataSourceConfigDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 15:43
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapper<DataSourceConfigDO> {
}
