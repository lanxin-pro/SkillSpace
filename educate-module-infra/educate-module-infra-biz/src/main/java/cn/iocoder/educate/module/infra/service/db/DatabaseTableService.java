package cn.iocoder.educate.module.infra.service.db;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * 数据库表 Service
 *
 * @Author: j-sentinel
 * @Date: 2023/7/23 15:35
 */
public interface DatabaseTableService {

    /**
     * 获得表列表，基于表名称 + 表描述进行模糊匹配
     *
     * @param dataSourceConfigId 数据源配置的编号
     * @param name 表名称，模糊匹配
     * @param comment 表描述，模糊匹配
     * @return 表列表
     */
    List<TableInfo> getTableList(Long dataSourceConfigId, String name, String comment);

    /**
     * 获得指定表名
     *
     * @param dataSourceConfigId 数据源配置的编号
     * @param tableName 表名称
     * @return 表
     */
    TableInfo getTable(Long dataSourceConfigId, String tableName);
}
