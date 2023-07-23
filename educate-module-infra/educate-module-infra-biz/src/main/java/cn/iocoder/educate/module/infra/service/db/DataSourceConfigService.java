package cn.iocoder.educate.module.infra.service.db;

import cn.iocoder.educate.module.infra.dal.dataobject.db.DataSourceConfigDO;

import java.util.List;

/**
 * 数据源配置 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/23 15:38
 */
public interface DataSourceConfigService {

    /**
     * 获得数据源配置
     *
     * @param dataSourceConfigId 编号
     * @return 数据源配置
     */
    DataSourceConfigDO getDataSourceConfig(Long dataSourceConfigId);

    /**
     * 获得数据源配置列表
     *
     * @return 数据源配置列表
     */
    List<DataSourceConfigDO> getDataSourceConfigList();

}
