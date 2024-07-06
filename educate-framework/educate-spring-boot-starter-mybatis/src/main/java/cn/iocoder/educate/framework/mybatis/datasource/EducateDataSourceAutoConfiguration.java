package cn.iocoder.educate.framework.mybatis.datasource;

import com.alibaba.druid.spring.boot3.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库配置类
 * @EnableConfigurationProperties(DruidStatProperties.class) 使用alibaba的Druid连接池，在新版中必须声明
 *
 * @author j-sentinel
 * @date 2024/7/6 9:30
 */
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true) // 启动事务管理
@EnableConfigurationProperties(DruidStatProperties.class)
public class EducateDataSourceAutoConfiguration {

}
