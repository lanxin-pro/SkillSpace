package cn.iocoder.educate.framework.mybatis.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.util.collection.SetUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 13:45
 */
@Slf4j
public class IdTypeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    /**
     * 淦！！！创建两次bean就创建两次bean
     * @param environment
     * @return
     */
    @Bean
    public String a(ConfigurableEnvironment environment){
        new IdTypeEnvironmentPostProcessor().postProcessEnvironment(environment,null);
        return "success";
    }

    private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";

    private static final String DATASOURCE_DYNAMIC_KEY = "spring.datasource.dynamic";

    private static final Set<DbType> INPUT_ID_TYPES = SetUtils.asSet(DbType.ORACLE, DbType.ORACLE_12C,
            DbType.POSTGRE_SQL, DbType.KINGBASE_ES, DbType.DB2, DbType.H2);

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 如果获取不到 DbType，则不进行处理
        DbType dbType = getDbType(environment);
        if(ObjectUtil.isEmpty(dbType)){
            return;
        }

        // 如果非 NONE，则不进行处理
        IdType idType = getIdType(environment);
        if (idType != IdType.NONE) {
            return;
        }
        // 情况一，用户输入 ID，适合 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库
        if (INPUT_ID_TYPES.contains(dbType)) {
            setIdType(environment, IdType.INPUT);
            return;
        }
        // 情况二，自增 ID，适合 MySQL 等直接自增的数据库
        setIdType(environment, IdType.AUTO);
    }

    public void setIdType(ConfigurableEnvironment environment, IdType idType) {
        environment.getSystemProperties().put(ID_TYPE_KEY, idType);
        log.info("[setIdType][修改 MyBatis Plus 的 idType 为({})]", idType);
    }

    public IdType getIdType(ConfigurableEnvironment environment) {
        return environment.getProperty(ID_TYPE_KEY, IdType.class);
    }

    private static DbType getDbType(ConfigurableEnvironment environment){
        // 这个是判断我有没有写primary这个选项，这个有默认的选项
        String primary = environment.getProperty(DATASOURCE_DYNAMIC_KEY + "." + "primary");
        if(StrUtil.isEmpty(primary)){
            return null;
        }
        String url = environment.getProperty(DATASOURCE_DYNAMIC_KEY + ".datasource." + primary + ".url");
        if(StrUtil.isEmpty(url)){
            return null;
        }
        return JdbcUtils.getDbType(url);
    }

}
