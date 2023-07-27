package cn.iocoder.educate.module.infra.service.codegen.inner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.date.DateUtils;
import cn.iocoder.educate.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.educate.framework.common.util.object.ObjectUtils;
import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.framework.operatelog.core.enums.OperateTypeEnum;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.enums.codegen.CodegenFrontTypeEnum;
import cn.iocoder.educate.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.educate.module.infra.framwork.codegen.config.CodegenProperties;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.map.MapUtil.getStr;

/**
 * 代码生成的引擎，用于具体生成代码
 *
 * 目前基于 {@link org.apache.velocity.app.Velocity} 模板引擎实现
 *
 * 考虑到 Java 模板引擎的框架非常多，Freemarker、Velocity、Thymeleaf 等等，
 * 所以我们采用 hutool 封装的 {@link cn.hutool.extra.template.Template} 抽象
 *
 * @Author: j-sentinel
 * @Date: 2023/7/27 13:20
 */
@Slf4j
@Component
public class CodegenEngine {

    /**
     * 后端的模板配置
     *
     * key：模板在 resources 的地址
     * value：生成的路径
     */
    private static final Map<String, String> SERVER_TEMPLATES = MapUtil.<String, String>
            // 有序
            builder(new LinkedHashMap<>())
            // 第一个参数是文件路径，第二哥参数是
            .put(javaTemplatePath("controller/vo/baseVO"), javaModuleImplVOFilePath("BaseVO"))
            .build();

    /**
     * 后端的配置模版
     * 后续也会直接拿 globalBindingMap 里面的值来替换
     *
     * key1：UI 模版的类型 {@link CodegenFrontTypeEnum#getType()}
     * key2：模板在 resources 的地址
     * value：生成的路径
     */
    private static final Table<Integer, String, String> FRONT_TEMPLATES = ImmutableTable.<Integer, String, String>builder()
            // Vue3 标准模版
            .put(CodegenFrontTypeEnum.VUE3.getType(), vue3TemplatePath("views/index.vue"),
                    vue3FilePath("views/${table.moduleName}/${classNameVar}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3.getType(), vue3TemplatePath("views/form.vue"),
                    vue3FilePath("views/${table.moduleName}/${classNameVar}/${simpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE3.getType(), vue3TemplatePath("api/api.ts"),
                    vue3FilePath("api/${table.moduleName}/${classNameVar}/index.ts"))
            .build();


    @Resource
    private CodegenProperties codegenProperties;

    /**
     * 全局通用变量映射
     * 为什么不添加 volatile 字符呢！  volatile的写内存语义是直接刷新到主内存中，读的内存语义是直接从主内存中读取，从而保证了可见性。
     */
    private final Map<String, Object> globalBindingMap = new HashMap<>();

    /**
     * 模板引擎，由 hutool 实现
     */
    private final TemplateEngine templateEngine;

    /**
     * 更改模板
     */
    public CodegenEngine() {
        // 初始化 TemplateEngine 属性
        // TemplateConfig为模板引擎的选项，可选内容有字符编码、模板路径、模板加载方式等，默认通过模板字符串渲染
        TemplateConfig config = new TemplateConfig();
        // 类路径加载
        config.setResourceMode(TemplateConfig.ResourceMode.CLASSPATH);
        // Velocity Thymeleaf FreeMarker Rythm Enjoy Beetl... 等等有很多模板语言
        this.templateEngine = new VelocityEngine(config);
    }

    @PostConstruct
    private void initGlobalBindingMap() {
        // 全局配置
        globalBindingMap.put("basePackage", codegenProperties.getBasePackage());
        // 用于后续获取测试类的 package 地址
        globalBindingMap.put("baseFrameworkPackage", codegenProperties.getBasePackage()
                + '.' + "framework");
        // 全局 Java Bean
        globalBindingMap.put("CommonResultClassName", CommonResult.class.getName());
        globalBindingMap.put("PageResultClassName", PageResult.class.getName());
        // VO 类，独有字段
        globalBindingMap.put("PageParamClassName", PageParam.class.getName());
        // DO 类，独有字段
        globalBindingMap.put("BaseDOClassName", BaseDO.class.getName());
        globalBindingMap.put("baseDOFields", CodegenBuilder.BASE_DO_FIELDS);
        globalBindingMap.put("QueryWrapperClassName", LambdaQueryWrapper.class.getName());
        globalBindingMap.put("BaseMapperClassName", BaseMapper.class.getName());
        // Util 工具类
        globalBindingMap.put("ServiceExceptionUtilClassName", ServiceExceptionUtil.class.getName());
        globalBindingMap.put("DateUtilsClassName", DateUtils.class.getName());
        globalBindingMap.put("LocalDateTimeUtilsClassName", LocalDateTimeUtils.class.getName());
        globalBindingMap.put("ObjectUtilsClassName", ObjectUtils.class.getName());
        globalBindingMap.put("OperateLogClassName", OperateLog.class.getName());
        globalBindingMap.put("OperateTypeEnumClassName", OperateTypeEnum.class.getName());
        log.info("[initLocalCache][缓存代码生成器Velocity模版，数量为:{}]", globalBindingMap.size());
    }

    public Map<String, String> execute(CodegenTableDO tableDO, List<CodegenColumnDO> columnsDO) {
        // 创建 bindingMap
        Map<String, Object> bindingMap = new HashMap<>(globalBindingMap);
        // 表的实体
        bindingMap.put("table", tableDO);
        // 字段实体
        bindingMap.put("columns", columnsDO);
        // 主键字段 filter
        bindingMap.put("primaryColumn", CollUtil.findOne(columnsDO, CodegenColumnDO::getPrimaryKey));
        bindingMap.put("sceneEnum", CodegenSceneEnum.valueOf(tableDO.getScene()));

        // 执行生成（前端和后端模板）
        Map<String, String> templates = getTemplates(tableDO.getFrontType());
        // 有序
        Map<String, String> result = Maps.newLinkedHashMapWithExpectedSize(templates.size());
        // vmPath就是模板文件路径参数，生成项目文件路径filePath
        templates.forEach((vmPath, filePath) -> {
            // 使用bindingMap来替换filePath中的${}
            filePath = formatFilePath(filePath, bindingMap);
            // 将模板与绑定参数融合后返回为字符串
            String content = templateEngine.getTemplate(vmPath).render(bindingMap);
            // 去除字段后面多余的 , 逗号
            // content = content.replaceAll(",\n}", "\n}").replaceAll(",\n  }", "\n  }");
            result.put(filePath, content);
        });
        return result;
    }

    private Map<String, String> getTemplates(Integer frontType) {
        Map<String, String> templates = new LinkedHashMap<>();
        // 后端参数
        templates.putAll(SERVER_TEMPLATES);
        // 前端参数
        // 在put的时候有三个参数，第一个参数是row的匹配项
        templates.putAll(FRONT_TEMPLATES.row(frontType));
        return templates;
    }

    private String formatFilePath(String filePath, Map<String, Object> bindingMap) {
        // replace(字符串,被查找的字符串,被替换的字符串)
        filePath = StrUtil.replace(filePath, "${basePackage}",
                getStr(bindingMap, "basePackage")
                        .replaceAll("\\.", "/"));
        filePath = StrUtil.replace(filePath, "${classNameVar}",
                getStr(bindingMap, "classNameVar"));
        filePath = StrUtil.replace(filePath, "${simpleClassName}",
                getStr(bindingMap, "simpleClassName"));
        // sceneEnum 包含的字段
        CodegenSceneEnum sceneEnum = (CodegenSceneEnum) bindingMap.get("sceneEnum");
        filePath = StrUtil.replace(filePath, "${sceneEnum.prefixClass}", sceneEnum.getPrefixClass());
        filePath = StrUtil.replace(filePath, "${sceneEnum.basePackage}", sceneEnum.getBasePackage());
        // table 包含的字段
        CodegenTableDO table = (CodegenTableDO) bindingMap.get("table");
        filePath = StrUtil.replace(filePath, "${table.moduleName}", table.getModuleName());
        filePath = StrUtil.replace(filePath, "${table.businessName}", table.getBusinessName());
        filePath = StrUtil.replace(filePath, "${table.className}", table.getClassName());
        return filePath;
    }

    /**
     * 后端模板路径生成
     * @param path
     * @return
     */
    private static String javaTemplatePath(String path) {
        return "codegen/java/" + path + ".vm";
    }

    /**
     * 后端真实项目路径生成
     * @param path 名称
     * @return
     */
    private static String javaModuleImplVOFilePath(String path) {
        return javaModuleFilePath("controller/${sceneEnum.basePackage}/${table.businessName}/" +
                "vo/${sceneEnum.prefixClass}${table.className}" + path, "biz", "main");
    }

    /**
     * @param path
     * @param module 模块的实现类还是接口类的名称
     * @param src 分支
     * @return
     */
    private static String javaModuleFilePath(String path, String module, String src) {
        // 顶级模块
        return "educate-module-${table.moduleName}/" +
                // 子模块
                "educate-module-${table.moduleName}-" + module + "/" +
                "src/" + src + "/java/${basePackage}/module/${table.moduleName}/" + path + ".java";
    }

    /**
     * 前端模板路径生成
     * @param path
     * @return
     */
    private static String vue3TemplatePath(String path) {
        return "codegen/vue3/" + path + ".vm";
    }

    /**
     * 前端真实项目路径生成
     * @param path
     * @return
     */
    private static String vue3FilePath(String path) {
        // 顶级目录
        return "educate-ui-${sceneEnum.basePackage}-vue3/" +
                "src/" + path;
    }


}
