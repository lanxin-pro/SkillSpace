package cn.iocoder.educate.framework.mybatis.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/6 20:07
 */
@MapperScan(value = "${lanxin.info.base-package}", annotationClass = Mapper.class)
public class EducateMybatisAutoConfiguration {
}
