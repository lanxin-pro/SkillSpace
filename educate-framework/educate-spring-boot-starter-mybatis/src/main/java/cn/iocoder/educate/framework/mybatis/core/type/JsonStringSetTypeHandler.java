package cn.iocoder.educate.framework.mybatis.core.type;

import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Set;

/**
 * 参考 {@link com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler} 实现
 * Set<String> 的类型转换器实现类，对应数据库的 varchar 类型
 *
 * 例如说哦，{@link cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO#tags} 属性
 *
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:44
 */
public class JsonStringSetTypeHandler extends AbstractJsonTypeHandler<Object> {

    private static final TypeReference<Set<String>> TYPE_REFERENCE = new TypeReference<Set<String>>(){};

    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, TYPE_REFERENCE);
    }

    @Override
    protected String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }
}
