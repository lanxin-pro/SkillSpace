package cn.iocoder.educate.framework.mybatis.config;

import cn.iocoder.educate.framework.mybatis.core.handler.DefaultDBFieldHandler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/6 20:07
 */
@MapperScan(value = "${lanxin.info.base-package}", annotationClass = Mapper.class)
public class EducateMybatisAutoConfiguration {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 自动填充参数类
     * @return
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        // 自动填充参数类
        return new DefaultDBFieldHandler();
    }
}
