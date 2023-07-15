摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/ 「芋道源码」欢迎转载，保留摘要，谢谢！

- [1. 概述](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)

- [2. screw 简介](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)

- 3. 快速入门

    - [3.1 使用 Java 代码的方式](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)
    - [3.2 使用 Maven 插件的方式](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)

- [4. 生成实体类](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)

- [666. 彩蛋](http://www.iocoder.cn/Spring-Boot/DB-Doc-screw/)

------

------

# 1. 概述

闲来无事的周六，被 🐶 芳放了鸽子，只好蹲在被窝里研究会技术，安慰下自己受伤的心灵。

![鸽子](https://static.iocoder.cn/4a2418ec4682ee6e68fa9d24e1d9d696.jpg)

相信胖友们都**手写**过**数据库表结构文档**，一通无脑的 CV 大法之后，小几十页的 Word 文档就出炉了。

那么有没什么工具，可以帮我们偷懒高效的**自动**生成数据库表结构文档呢？有，通过使用 [screw](https://github.com/pingfangushi/screw) 生成工具。

# 2. screw 简介

screw 是一个简洁好用的数据库表结构文档的**生成工具**，支持 MySQL、Oracle、PostgreSQL 等主流的关系数据库。

> 良心艿：screw 的仓库地址是 <https://github.com/pingfangushi/screw>，感兴趣的胖友，可以研究一波。

生成的文档有 HTML、Word、Markdown 三种**格式**，示例如下图所示：

| 格式     | 图                                                           |
| -------- | ------------------------------------------------------------ |
| HTML     | ![HTML 示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/01.png) |
| Word     | ![Word 示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/02.png) |
| Markdown | ![Markdown 示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/03.png) |

# 3. 快速入门

screw 有两种**方式**来生成文档，通过 **Java 代码**或者 **Maven 插件**。

下面，我们来分别快速入门下。

## 3.1 使用 Java 代码的方式

创建 [`lab-70-db-doc-screw-01`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-01) 示例项目，使用 screw 的 **Java 代码**的方式，生成文档。

![示例示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/11.png)

> 友情提示：示例代码的完整地址，可见 <https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-01>。

### 3.1.1 引入依赖

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-70-db-doc/lab-70-db-doc-screw-01/pom.xml) 文件中，引入 screw 的**依赖** [`screw-core`](https://mvnrepository.com/artifact/cn.smallbun.screw/screw-core)。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>lab-70-db-doc</artifactId>
        <groupId>cn.iocoder.springboot.labs</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-70-db-doc-screw-01</artifactId>

    <dependencies>
        <!-- screw 库，简洁好用的数据库表结构文档生成器 -->
        <dependency>
            <groupId>cn.smallbun.screw</groupId>
            <artifactId>screw-core</artifactId>
            <version>1.0.5</version>
        </dependency>

        <!-- 数据库连接 -->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
            <version>3.4.5</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.22</version>
        </dependency>
    </dependencies>

</project>
```

依赖 `HikariCP` 和 `mysql-connector-java` 是为了连接 MySQL 数据库。

### 3.1.2 ScrewMain

创建 [ScrewMain](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-70-db-doc/lab-70-db-doc-screw-01/src/main/java/ScrewMain.java) 类，使用 screw 生成文档。代码如下：

```
public class ScrewMain {

    private static final String DB_URL = "jdbc:mysql://400-infra.server.iocoder.cn:3306";
    private static final String DB_NAME = "mall_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "3WLiVUBEwTbvAfsh";

    private static final String FILE_OUTPUT_DIR = "/Users/yunai/screw_test";
    private static final EngineFileType FILE_OUTPUT_TYPE = EngineFileType.HTML; // 可以设置 Word 或者 Markdown 格式
    private static final String DOC_FILE_NAME = "数据库文档";
    private static final String DOC_VERSION = "1.0.0";
    private static final String DOC_DESCRIPTION = "文档描述";

    public static void main(String[] args) {
        // 创建 screw 的配置
        Configuration config = Configuration.builder()
                .version(DOC_VERSION)  // 版本
                .description(DOC_DESCRIPTION) // 描述
                .dataSource(buildDataSource()) // 数据源
                .engineConfig(buildEngineConfig()) // 引擎配置
                .produceConfig(buildProcessConfig()) // 处理配置
                .build();

        // 执行 screw，生成数据库文档
        new DocumentationExecute(config).execute();
    }

    /**
     * 创建数据源
     */
    private static DataSource buildDataSource() {
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(DB_URL + "/" + DB_NAME);
        hikariConfig.setUsername(DB_USERNAME);
        hikariConfig.setPassword(DB_PASSWORD);
        hikariConfig.addDataSourceProperty("useInformationSchema", "true"); // 设置可以获取 tables remarks 信息
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 创建 screw 的引擎配置
     */
    private static EngineConfig buildEngineConfig() {
        return EngineConfig.builder()
                .fileOutputDir(FILE_OUTPUT_DIR) // 生成文件路径
                .openOutputDir(false) // 打开目录
                .fileType(FILE_OUTPUT_TYPE) // 文件类型
                .produceType(EngineTemplateType.freemarker) // 文件类型
                .fileName(DOC_FILE_NAME) // 自定义文件名称
                .build();
    }

    /**
     * 创建 screw 的处理配置，一般可忽略
     * 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
     */
    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
                .designatedTableName(Collections.<String>emptyList())  // 根据名称指定表生成
                .designatedTablePrefix(Collections.<String>emptyList()) //根据表前缀生成
                .designatedTableSuffix(Collections.<String>emptyList()) // 根据表后缀生成
                .ignoreTableName(Arrays.asList("test_user", "test_group")) // 忽略表名
                .ignoreTablePrefix(Collections.singletonList("test_")) // 忽略表前缀
                .ignoreTableSuffix(Collections.singletonList("_test")) // 忽略表后缀
                .build();
    }

}
```

代码比较简单，胖友看看注释即可。

比较重要的变量，已经抽取成**静态**变量，胖友看着修改哈。

### 3.1.3 简单测试

执行 ScrewMain 程序，进行文档的生成。这里我们生成的是 HTML 文档，效果如下图所示：

![HTML 文档](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/12.png)

## 3.2 使用 Maven 插件的方式

创建 [`lab-70-db-doc-screw-02`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-02) 示例项目，使用 screw 的 **Maven 插件**的方式，生成文档。

![示例示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/21.png)

> 友情提示：示例代码的完整地址，可见 <https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-02>。

### 3.2.1 引入插件

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-70-db-doc/lab-70-db-doc-screw-02/pom.xml) 文件中，引入 screw 的**插件** [`screw-maven-plugin`](https://mvnrepository.com/artifact/cn.smallbun.screw/screw-maven-plugin)。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>lab-70-db-doc</artifactId>
        <groupId>cn.iocoder.springboot.labs</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-70-db-doc-screw-02</artifactId>

    <build>
        <plugins>
            <plugin>
                <groupId>cn.smallbun.screw</groupId>
                <artifactId>screw-maven-plugin</artifactId>
                <version>1.0.5</version>
                <dependencies>
                    <!-- 数据库连接 -->
                    <dependency>
                        <groupId>com.zaxxer</groupId>
                        <artifactId>HikariCP</artifactId>
                        <version>3.4.5</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.22</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <!-- 数据库相关配置 -->
                    <driverClassName>com.mysql.cj.jdbc.Driver</driverClassName>
                    <jdbcUrl>jdbc:mysql://400-infra.server.iocoder.cn:3306/mall_system</jdbcUrl>
                    <username>root</username>
                    <password>3WLiVUBEwTbvAfsh</password>
                    <!-- screw 配置 -->
                    <fileType>HTML</fileType>
                    <title>数据库文档</title> <!--标题-->
                    <fileName>测试文档名称</fileName> <!--文档名称 为空时:将采用[数据库名称-描述-版本号]作为文档名称-->
                    <description>数据库文档生成</description> <!--描述-->
                    <version>${project.version}</version> <!--版本-->
                    <openOutputDir>false</openOutputDir> <!--打开文件输出目录-->
                    <produceType>freemarker</produceType> <!--生成模板-->
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

Maven 插件的**配置项**比较少，胖友按需修改下 `<configuration/>` 即可。

### 3.2.2 简单测试

执行 `screw-maven-plugin` 插件，会在 `doc` 目录下生成文档。如下图所示：

![执行 Maven 插件](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/22.png)

# 4. 生成实体类

screw 的实现原理，是基于数据库中的**表结构**，生成对应的文档。

那么，是否可以**自动**生成 Java 实体类呢？答案是可以的，在 [`screw-extension`](https://github.com/pingfangushi/screw/tree/master/screw-extension) 项目中，**拓展**提供了该功能。

这样，日常开发中，在我们完成数据库的**建表**之后，可以直接生成对应的 Java 实体类，避免枯燥的重复劳动。

下面，我们来快速入门下。创建 [`lab-70-db-doc-screw-03`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-03) 示例项目，使用 screw 的 **Java 代码**的方式，生成 Java 实体类。

![示例示例](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/31.png)

> 友情提示：示例代码的完整地址，可见 <https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-70-db-doc/lab-70-db-doc-screw-03>。

## 4.1 引入依赖

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-70-db-doc/lab-70-db-doc-screw-03/pom.xml) 文件中，**额外**引入 screw 的**依赖** `screw-extension`。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>lab-70-db-doc</artifactId>
        <groupId>cn.iocoder.springboot.labs</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-70-db-doc-screw-03</artifactId>

    <dependencies>
        <!-- screw 库，简洁好用的数据库表结构文档生成器 -->
        <dependency>
            <groupId>cn.smallbun.screw</groupId>
            <artifactId>screw-core</artifactId>
            <version>1.0.5</version>
        </dependency>
        <dependency>
            <groupId>cn.smallbun.screw</groupId>
            <artifactId>screw-extension</artifactId>
            <version>1.0.5</version>
        </dependency>

        <!-- 数据库连接 -->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
            <version>3.4.5</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.22</version>
        </dependency>
    </dependencies>

</project>
```

> 友情提示：由于 `screw-extension` 项目处于初步开发阶段，暂时未将该依赖推到 Maven 中央仓库，所以需要自己克隆[项目](https://gitee.com/leshalv/screw)，进行编译打包到本地。

## 4.2 ScrewMain

创建 [ScrewMain](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-70-db-doc/lab-70-db-doc-screw-03/src/main/java/ScrewMain.java) 类，使用 screw 生成 Java 实体类。代码如下：

```
public class ScrewMain {

    private static final String DB_URL = "jdbc:mysql://400-infra.server.iocoder.cn:3306";
    private static final String DB_NAME = "mall_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "3WLiVUBEwTbvAfsh";

    private static final String FILE_OUTPUT_DIR = "/Users/yunai/screw_test";
    private static final String JAVA_CLASS_PACKAGE = "cn.iocoder.dataobject";

    public static void main(String[] args) {
        // 创建 screw 的配置
        PojoConfiguration config = PojoConfiguration.builder()
                .path(FILE_OUTPUT_DIR) // 生成 POJO 相关的目录
                .packageName(JAVA_CLASS_PACKAGE) // 包名
                .nameStrategy(new HumpNameStrategy()) // 包名策略
                .useLombok(false) // 是否使用 Lombok
                .dataSource(buildDataSource()) // 数据源
                .processConfig(buildProcessConfig()) // 处理配置
                .build();

        // 执行 screw，生成 POJO 实体类
        new PojoExecute(config).execute();
    }

    /**
     * 创建数据源
     */
    private static DataSource buildDataSource() {
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(DB_URL + "/" + DB_NAME);
        hikariConfig.setUsername(DB_USERNAME);
        hikariConfig.setPassword(DB_PASSWORD);
        hikariConfig.addDataSourceProperty("useInformationSchema", "true"); // 设置可以获取 tables remarks 信息
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 创建 screw 的处理配置，一般可忽略
     * 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
     */
    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
                .designatedTableName(Collections.<String>emptyList())  // 根据名称指定表生成
                .designatedTablePrefix(Collections.<String>emptyList()) //根据表前缀生成
                .designatedTableSuffix(Collections.<String>emptyList()) // 根据表后缀生成
                .ignoreTableName(Arrays.asList("test_user", "test_group")) // 忽略表名
                .ignoreTablePrefix(Collections.singletonList("test_")) // 忽略表前缀
                .ignoreTableSuffix(Collections.singletonList("_test")) // 忽略表后缀
                .build();
    }

}
```

代码比较简单，胖友看看注释即可。不同于上面的快速入门，这里我们使用 [PojoConfiguration](https://github.com/pingfangushi/screw/blob/master/screw-extension/src/main/java/cn/smallbun/screw/extension/pojo/PojoConfiguration.java) 作为配置类，使用 [PojoExecute](https://github.com/pingfangushi/screw/blob/master/screw-extension/src/main/java/cn/smallbun/screw/extension/pojo/execute/PojoExecute.java) 作为执行器。

比较重要的变量，已经抽取成**静态**变量，胖友看着修改哈。

## 4.3 简单测试

执行 ScrewMain 程序，进行 Java 实体类的生成。效果如下图所示：

![Java 实体类](https://static.iocoder.cn/images/Spring-Boot/2019-11-20/32.png)

生成的 Java 实体类的**成员属性**还不太正确，需要等待作者进行下修复。

# 666. 彩蛋

至此，我们已经完成 screw 的学习，一起来简单总结下：

- screw 支持生成**数据库表结构文档**，通过 Java 代码或者 Maven 插件的方式。
- screw 支持生成 **Java 实体类**，通过 `screw-extension` 提供。

初略喵了下 screw 的代码，总体代码量在 5000+ 行，核心代码在 2000 行左右。项目分层干净，代码注释完成，胖友可以选择阅读了解下。这样，我们可以根据我们实际项目的需要，进行**二次开发**。

------

