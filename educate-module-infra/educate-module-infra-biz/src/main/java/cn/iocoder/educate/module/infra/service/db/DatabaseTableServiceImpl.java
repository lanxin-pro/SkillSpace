package cn.iocoder.educate.module.infra.service.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.module.infra.dal.dataobject.db.DataSourceConfigDO;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 15:35
 */
@Slf4j
@Service
public class DatabaseTableServiceImpl implements DatabaseTableService {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public List<TableInfo> getTableList(Long dataSourceConfigId, String nameLike, String commentLike) {
        List<TableInfo> tables = getTableList0(dataSourceConfigId, null);
        // 条件查询         只有返回值为 true 的表格信息会被保留下来
        return tables.stream().filter(tableInfo -> {
            // 为true代表排除,为null为true,有值为false，需要执行后面的比较
            return (StrUtil.isEmpty(nameLike) || tableInfo.getName().contains(nameLike))
                    && (StrUtil.isEmpty(commentLike) || tableInfo.getComment().contains(commentLike));
        }).collect(Collectors.toList());
    }

    @Override
    public TableInfo getTable(Long dataSourceConfigId, String tableName) {
        List<TableInfo> tableList0 = getTableList0(dataSourceConfigId, tableName);
        // 只返回一个，防止返回多个出现错误
        return CollUtil.getFirst(tableList0);
    }

    private List<TableInfo> getTableList0(Long dataSourceConfigId, String name) {
        // 获得数据源配置tableList0 = {ArrayList@19558}  size = 1
        DataSourceConfigDO config = dataSourceConfigService.getDataSourceConfig(dataSourceConfigId);
        Assert.notNull(config, "数据源({}) 不存在！", dataSourceConfigId);

        // 使用 MyBatis Plus Generator 解析表结构
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()).build();
        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder();
        if (StrUtil.isNotEmpty(name)) {
            strategyConfig.addInclude(name);
        } else {
            // 移除工作流和定时任务前缀的表名 // TODO j-sentinel 未来做成可配置
            strategyConfig.addExclude("ACT_[\\S\\s]+|QRTZ_[\\S\\s]+|FLW_[\\S\\s]+");
        }
        // 只使用 Date 类型，不使用 LocalDate
        GlobalConfig globalConfig = new GlobalConfig.Builder().dateType(DateType.TIME_PACK).build();
        ConfigBuilder builder = new ConfigBuilder(null, dataSourceConfig, strategyConfig.build(),
                null, globalConfig, null);
        // 按照名字排序
        List<TableInfo> tables = builder.getTableInfoList();
        tables.sort(Comparator.comparing(TableInfo::getName));
        return tables;
    }

}
