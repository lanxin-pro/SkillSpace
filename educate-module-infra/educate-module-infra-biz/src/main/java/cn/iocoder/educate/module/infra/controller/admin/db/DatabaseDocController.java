package cn.iocoder.educate.module.infra.controller.admin.db;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/15 11:05
 */
@Tag(name = "管理后台 - 数据库文档")
@RestController
@RequestMapping("/infra/db-doc")
public class DatabaseDocController {

    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    // c盘的APPData
    private static final String FILE_OUTPUT_DIR = System.getProperty("java.io.tmpdir") + File.separator
            + "db-doc";

    private static final String DOC_FILE_NAME = "MYSQL数据库文档";

    private static final String DOC_VERSION = "1.0.1";

    private static final String DOC_DESCRIPTION = "文档描述，唔 实在想不出来写什么了";


    @GetMapping("/export-html")
    @Operation(summary = "导出 html 格式的数据文档")
    @Parameter(name = "deleteFile", description = "是否删除在服务器本地生成的数据库文档", example = "true")
    public void exportHtml(@RequestParam(defaultValue = "true") Boolean deleteFile,
                           HttpServletResponse response) throws IOException {
        doExportFile(EngineFileType.HTML, deleteFile, response);
    }

    @GetMapping("/export-word")
    @Operation(summary = "导出 word 格式的数据文档")
    @Parameter(name = "deleteFile", description = "是否删除在服务器本地生成的数据库文档", example = "true")
    public void exportWord(@RequestParam(defaultValue = "true") Boolean deleteFile,
                           HttpServletResponse response) throws IOException {
        doExportFile(EngineFileType.WORD, deleteFile, response);
    }

    @GetMapping("/export-markdown")
    @Operation(summary = "导出 markdown 格式的数据文档")
    @Parameter(name = "deleteFile", description = "是否删除在服务器本地生成的数据库文档", example = "true")
    public void exportMarkdown(@RequestParam(defaultValue = "true") Boolean deleteFile,
                               HttpServletResponse response) throws IOException {
        doExportFile(EngineFileType.MD, deleteFile, response);
    }

    private void doExportFile(EngineFileType fileOutputType, Boolean deleteFile,
                              HttpServletResponse response) throws IOException {
        // 随机的文件名
        String docFileName = DOC_FILE_NAME + "_" + IdUtil.fastSimpleUUID();
        // 生成的文档
        String filePath = doExportFile(fileOutputType, docFileName);
        // 下载后的文件名
        String downloadFileName = DOC_FILE_NAME + fileOutputType.getFileSuffix();
        try {
            // 读取，返回
            ServletUtils.writeAttachment(response, downloadFileName, FileUtil.readBytes(filePath));
        } finally {
            // 删除文件，防止不停的创建，导致文件过大
            handleDeleteFile(deleteFile, filePath);
        }
    }

    /**
     * 输出文件，返回文件路径
     *
     * @param fileOutputType 文件类型
     * @param fileName 文件名, 无需 ".docx" 等文件后缀
     * @return 生成的文件所在路径
     */
    private String doExportFile(EngineFileType fileOutputType, String fileName) {
        try (HikariDataSource dataSource = buildDataSource()) {
            // 创建 screw 的配置
            Configuration config = Configuration.builder()
                    // 版本
                    .version(DOC_VERSION)
                    // 描述
                    .description(DOC_DESCRIPTION)
                    // 数据源
                    .dataSource(dataSource)
                    // 引擎配置
                    .engineConfig(buildEngineConfig(fileOutputType, fileName))
                    // 处理配置
                    .produceConfig(buildProcessConfig())
                    .build();

            // 执行 screw，生成数据库文档
            new DocumentationExecute(config).execute();

            return FILE_OUTPUT_DIR + File.separator + fileName + fileOutputType.getFileSuffix();
        }
    }

    /**
     * 创建 screw 的引擎配置
     */
    private static EngineConfig buildEngineConfig(EngineFileType fileOutputType, String docFileName) {
        return EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(FILE_OUTPUT_DIR)
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(fileOutputType)
                // 文件类型
                .produceType(EngineTemplateType.velocity)
                // 自定义文件名称
                .fileName(docFileName)
                .build();
    }

    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
                // 忽略表前缀
                .ignoreTablePrefix(Arrays.asList("QRTZ_", "ACT_"))
                .build();
    }

    /**
     * 创建数据源
     *
     * TODO j-sentinel：screw 暂时不支持 druid
     * @return
     */
    private HikariDataSource buildDataSource() {
        // 获得 DataSource 数据源，目前只支持首个
        String primary = dynamicDataSourceProperties.getPrimary();
        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get(primary);
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceProperty.getUrl());
        hikariConfig.setUsername(dataSourceProperty.getUsername());
        hikariConfig.setPassword(dataSourceProperty.getPassword());
        // 设置可以获取 tables remarks 信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    private void handleDeleteFile(Boolean deleteFile, String filePath) {
        if (!deleteFile) {
            return;
        }
        FileUtil.del(filePath);
    }
}
