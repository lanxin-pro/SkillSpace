摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/Unit-Test/ 「芋道源码」欢迎转载，保留摘要，谢谢！

- [1. 概述](http://www.iocoder.cn/Spring-Boot/Unit-Test/)
- [2. 快速入门](http://www.iocoder.cn/Spring-Boot/Unit-Test/)
- [3. 不会写单元测试](http://www.iocoder.cn/Spring-Boot/Unit-Test/)
- [666. 彩蛋](http://www.iocoder.cn/Spring-Boot/Unit-Test/)

------

------

> 本文在提供完整代码示例，可见 <https://github.com/YunaiV/SpringBoot-Labs> 的 [lab-42](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-42) 目录。
>
> 原创不易，给点个 [Star](https://github.com/YunaiV/SpringBoot-Labs/stargazers) 嘿，一起冲鸭！

# 1. 概述

本文，我们来学习下如何在 Spring Boot 下进行单元测试（Unit Test）。在开始阅读之前，胖友先阅读[《小谈 Java 单元测试》](http://www.iocoder.cn/Fight/A-little-bit-about-Java-unit-testing/?self)文章，对测试有一个简单的了解，特别是要区分清楚什么是单元测试，什么是集成测试（Integration Test）。

在 Spring Boot 项目中，有一个 [spring-boot-test](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-test) 模块，封装了 Spring Boot 对 Test 的封装。例如说，提供了 [`@MockBean`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-test/src/main/java/org/springframework/boot/test/mock/mockito/MockBean.java) 注解，创建一个使用 [Mockito](https://site.mockito.org/) 进行 Mock 的 Bean，注入到依赖该 Bean 的其它 Bean 中。😈 如果有点懵逼，没关系，下面我会提供该注解的使用示例。

另外，Spring 项目中，也有一个 [spring-test](https://github.com/spring-projects/spring-framework/tree/master/spring-test) 模块，封装了 Spring 对 Test 的封装。例如说，提供了 [MockMvc](https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/main/java/org/springframework/test/web/servlet/MockMvc.java) 类，允许我们方便的对 SpringMVC 进行 Mock。😈 如果有点懵逼，没关系，下面我会提供该类的使用示例。

不过噢，本文我们并不会特别深入的讲单元测试的使用，而是侧重在 Spring Boot 中，如何编写单元测试，针对每一个层级。例如说，Controller 层、Service 层、Dao 层。当然，胖友也不用担心，阅读完本文之后，推荐阅读下[《有效的单元测试》](https://item.jd.com/31646890011.html)书籍，非常非常非常不错哈。

下面，开始我们的单元测试的学习之旅，哇哈哈。

# 2. 快速入门

> 示例代码对应仓库：[lab-42-demo01](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-42/lab-42-demo01/)。

本小节，我们先来搭建一个提供读取指定用户信息的 HTTP API 的 Spring Boot 应用，然后编写该 HTTP API 涉及到的 Controller、Service、Dao 的单元测试。

## 2.1 引入依赖

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-42-demo01</artifactId>

    <dependencies>
        <!-- 实现对 Spring MVC 的自动化配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 实现对数据库连接池的自动化配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency> <!-- 本示例，我们使用 MySQL -->
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>

        <!-- 方便等会写单元测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId> <!-- 单元测试，我们采用 H2 作为数据库 -->
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

- 引入 `spring-boot-starter-test` 依赖，实现对 Spring Boot Test 的自动化配置。在其中，它会引入 [JUnit](https://junit.org/junit5/)、[Mockito](https://site.mockito.org/)、[Hamcrest](http://hamcrest.org/) 等等框架，用于编写测试。
- 引入 `com.h2database.h2` 依赖，因为晚点编写 Dao 的单元测试时，我们显然不能使用 MySQL，这样会存在对外部环境的依赖，所以我们采用 H2 内存数据库。

## 2.2 配置文件

在 [`main/resources/`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/resources/) 目录下，我们创建**应用**的配置文件 [`application.yaml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/resources/application.yaml)。配置如下：

```
spring:
  # datasource 数据源配置内容
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/lab-39-mysql?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password:
```

- 😈 这里实际我们并不会使用到该配置文件，仅仅占个“坑”。稍后我们会使用测试目录下的配置文件，“覆盖”它。

在 [`test/resources/`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/resources/) 目录下，我们创建**测试**的配置文件。配置如下：

```
spring:
  # datasource 数据源配置内容
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

- 在 `spring.datasource.url` 配置项中，我们设置了 H2 内存数据库。通过这样的方式，我们在单元测试中，使用 H2 内存数据库，而不是 MySQL 数据库。

## 2.3 HTTP API

读取之地当用户信息 HTTP API，一共涉及如下四个类：

- [`controller/UserController`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/java/cn/iocoder/springboot/lab23/testdemo/controller/UserController.java)
- [`service/UserService`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/java/cn/iocoder/springboot/lab23/testdemo/service/UserService.java)
- [`dao/UserDao`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/java/cn/iocoder/springboot/lab23/testdemo/dao/UserDao.java)
- [`dataobject/UserDO`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/main/java/cn/iocoder/springboot/lab23/testdemo/dataobject/UserDO.java)

代码如下：

```
// UserController.java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获得指定用户编号的用户
     *
     * @param id 用户编号
     * @return 用户
     */
    @GetMapping("/get") // URL 修改成 /get
    public UserDO get(@RequestParam("id") Integer id) {
        // 查询并返回用户
        return userService.get(id);
    }

}

// UserService.java
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserDO get(Integer id) {
        return userDao.selectById(id);
    }

}

// UserDao.java
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate template;

    public UserDO selectById(Integer id) {
        return template.queryForObject("SELECT id, username, password FROM t_user WHERE id = ?",
                new BeanPropertyRowMapper<>(UserDO.class), // 结果转换成对应的对象
                id);
    }

}

// UserDO.java
public class UserDO {

    /**
     * 用户编号
     */
    private Integer id;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码（明文）
     *
     * ps：生产环境下，千万不要明文噢
     */
    private String password;
    
    // ... 省略 set/get 方法
}
```

- 按照 Controller => Service => Dao 调用，比较简单。

## 2.4 单元测试

下面，我们给[「2.3 HTTP API」](https://www.iocoder.cn/Spring-Boot/Unit-Test/?yudao#)涉及到的 Controller、Service、Dao 编写单元测试。

### 2.4.1 Controller 单元测试

在 [`test/java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/) 目录下，创建 [`cn.iocoder.springboot.lab23.testdemo.controller`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/controller/) 包，然后创建 UserController 对应的 [UserControllerTest](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/controller/UserControllerTest.java) 单元测试类。代码如下：

```
@RunWith(SpringRunner.class) // <1.1>
@SpringBootTest // <1.2>
@AutoConfigureMockMvc // <1.3>
public class UserControllerTest {

    @Autowired
    private MockMvc mvc; // <1.3>

    @MockBean
    private UserService userService; // <1.4>

    @Test
    public void testGet() throws Exception {
        // <2.1> Mock UserService 的 get 方法
        Mockito.when(userService.get(1)).thenReturn(
                new UserDO().setId(1).setUsername("username:1").setPassword("password:1"));

        // <2.2> 查询用户
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/user/get?id=1"));

        // <2.3> 校验响应状态码
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()); // 响应状态码 200

        // <2.4.1> 校验响应内容方式一：直接全部匹配
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\n" +
                "    \"id\": 1,\n" +
                "    \"username\": \"username:1\",\n" +
                "    \"password\": \"password:1\"\n" +
                "}", true)); // 响应结果

        // <2.4.2> 校验响应内容方式二：逐个字段匹配
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("id", IsEqual.equalTo(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("username", IsEqual.equalTo("username:1")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("password", IsEqual.equalTo("password:1")));
    }

}
```

> 友情提示：我们先来一起看看配置相关。

**<1.1> 处**，`@RunWith` 注解，是 JUnit 所提供。通过添加 `@RunWith(SpringRunner.class)`，告诉 JUnit 使用 [SpringRunner](https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/main/java/org/springframework/test/context/junit4/SpringRunner.java) 作为 Junit [Runner](https://github.com/junit-team/junit4/wiki/Test-runners)，从而在测试的时候，可以创建 Spring 容器。

**<1.2> 处**，`@SpringBootTest` 注解，表示基于 Spring Boot 的测试类。这样，我们就可以使用 Spring Boot Test 提供的功能。

**<1.3> 处**，`@AutoConfigureMockMvc` 注解，用于自动化配置我们稍后注入的 MockMvc Bean 对象 `mvc` 。在后续的测试中，我们会看到都是通过 `mvc` 调用后端 API 接口。

**<1.4> 处**，在类型为 UserService 的 `userService` 属性上，添加了 `@MockBean` 注解，创建了一个基于 Mockito 的 UserService Mock 代理对象 Bean。同时，该 Bean 会注入到依赖 UserService 的 UserController 中。这样，稍后我们就可以 mock `userService` 的方法，实现对 UserController 的单元测试。

> 友情提示：我们再来一起看看 `#testGet()` 方法，针对 `/user/get` 接口的单元测试。

**`<2.1>` 处，**对 `userService` 的 `#get(Integer id)` 方法进行 mock，当传入 `id=1` 时，返回指定的 UserDO 信息。

**<2.2> 处，使用 mvc 进行了一次 GET /user/get 调用，查询 id=1 的用户。😈 这里我们并未启动一个真实** 的 Web 容器，而是通过 MockMvc 模拟的方式。

调用完成后，会返回 [ResultActions](https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/main/java/org/springframework/test/web/servlet/ResultActions.java) 结果对象。通过它，我们可以进行打印请求结果、**断言**请求结果等等操作。下面，我们在 `<2.3>`、`<2.4>` 部分，就会对结果进行断言。

- `<2.3>` 处，校验响应状态码。
- `<2.4.1>` 处，校验响应内容方式一：直接全部匹配。
- `<2.4.2>` 处，校验响应内容方式二：逐个字段匹配。

🐶 一般情况下，因为 Controller 主要是对 Service 的简单调用，所以很多时候，我们并不会对其进行单元测试。在写本文的过程中，艿艿也找朋友问了一圈，基本如此。

### 2.4.2 Service 单元测试

在 [`test/java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/) 目录下，创建 [`cn.iocoder.springboot.lab23.testdemo.service`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/service/) 包，然后创建 UserService 对应的 [UserServiceTest](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/service/UserServiceTest.java) 单元测试类。代码如下：

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Test
    public void testGet() {
        // Mock UserDao 的 selectById 方法
        Mockito.when(userDao.selectById(1)).thenReturn(
                new UserDO().setId(1).setUsername("username:1").setPassword("password:1"));

        // 查询用户
        UserDO user = userService.get(1);

        // 校验结果
        Assert.assertEquals("编号不匹配", 1, (int) user.getId());
        Assert.assertEquals("用户名不匹配", "username:1", user.getUsername());
        Assert.assertEquals("密码不匹配", "password:1", user.getPassword());
    }

}
```

总体和[「2.4.1 Controller 单元测试」](https://www.iocoder.cn/Spring-Boot/Unit-Test/?yudao#)差不多，艿艿就不逐个细讲，而是说说差异点哈。

① 无需使用 `@AutoConfigureMockMvc` 注解，因为我们不需要使用 MockMvc。

② 对 UserDao 进行 mock。

③ 使用 JUnit 提供的 [Assert](https://github.com/junit-team/junit4/blob/master/src/main/java/org/junit/Assert.java) 类，进行结果的断言。

🐶 Service 的单元测试，是比较推荐去写的。不过考虑到日常开发都是比较忙的，不一定有时间去编写单元测试。这里艿艿建议：

- 对于逻辑复杂的 Service 方法，一定要去写单元测试，这样虽然多费点时间，但是绝对能够保证交付质量。同时我们会发现，为了更好的进行单元测试，我们会把一个大的方法，拆分成若干小的方法。
- 在测试阶段的时候，一般工作量会稍微小一点。此时，可以考虑补齐一些单元测试。虽然说，都已经开发完成，自己也测试过一轮了，但是想想，未来开发的时候，这些单元还是非常有用的呀。

### 2.4.3 Dao 单元测试

在 [`test/java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/) 目录下，创建 [`cn.iocoder.springboot.lab23.testdemo.dao`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/dao/) 包，然后创建 UserDao 对应的 [UserDaoTest](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-42/lab-42-demo01/src/test/java/cn/iocoder/springboot/lab23/testdemo/dao/UserDaoTest.java) 单元测试类。代码如下：

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Sql(scripts = "/sql/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO `t_user`(`id`, `username`, `password`) VALUES (1, 'username:1', 'password:1');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSelectById() {
        // 查询用户
        UserDO user = userDao.selectById(1);

        // 校验结果
        Assert.assertEquals("编号不匹配", 1, (int) user.getId());
        Assert.assertEquals("用户名不匹配", "username:1", user.getUsername());
        Assert.assertEquals("密码不匹配", "password:1", user.getPassword());
    }

}
```

针对 Dao 的测试，我们并不会使用 Mockito 进行 mock 的方式，而是使用**内存数据库**，进行对应的数据库操作。例如说，这里我们采用 H2 内存数据库。

在 `#testSelectById()` 方法上，我们使用 Spring Test 提供的 [`@Sql`](https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/main/java/org/springframework/test/context/jdbc/Sql.java) 注解，实现 `t_user` 表的创建，以及插入一条 `id=1` 的数据，这样我们就可以进行对应的单元测试。每一个 `@Sql` 注解的作用如下：

- 第一条，在单元测试方法执行

  之前

  ，执行



`/sql/create_tables.sql`



脚本，创建



  ```
  t_user
  ```



表。脚本如下：

  ```
  CREATE TABLE `t_user` (
     `id` INT AUTO_INCREMENT  PRIMARY KEY COMMENT '用户编号',
     `username` VARCHAR(64) NOT NULL COMMENT '账号',
     `password` VARCHAR(64) NOT NULL COMMENT '密码'
  );
  ```


- 第二条，在单元测试方法执行**之前**，执行在 `statements` 属性定义的 SQL 操作，插入一条 `id=1` 的数据。

- 第三条，在单元测试方法执行

  之后

  ，

  `/sql/clean.sql`



脚本，清空数据。毕竟，多个单元测试是共享一个 H2 内存数据库，所以需要进行清理。脚本如下：

  ```
  CREATE TABLE `t_user` (
     `id` INT AUTO_INCREMENT  PRIMARY KEY COMMENT '用户编号',
     `username` VARCHAR(64) NOT NULL COMMENT '账号',
     `password` VARCHAR(64) NOT NULL COMMENT '密码'
  );
  ```

🐶 因为 Dao 层，我们一般会采用 JPA、MyBatis 等等 ORM 框架，所以对它的单元测试，写的也是比较少的。

# 3. 不会写单元测试

对于初学单元测试的胖友，可能会碰到跟艿艿一样的困惑，怎么使用都学会了，一到项目中写单元测试，突然就一脸懵逼，到咋写呢？

这里，艿艿推荐胖友可以去看下 [Apollo](https://github.com/ctripcorp/apollo) 开源项目，由携程开源的分布式配置中心。在 Apollo 项目中，会有比较多的配置管理的**业务**逻辑，开发者对这些逻辑写了蛮多单元测试，我们可以进行借鉴学习，嘿嘿。

# 666. 彩蛋

😈 生活如此美好，本文到此拉倒。哈哈哈~再次推荐阅读下[《有效的单元测试》](https://item.jd.com/31646890011.html)书籍。

后续想要学习 JUnit 的胖友，可以推荐阅读如下文章：

- [《JUnit4 单元测试入门教程》](http://www.iocoder.cn/Fight/JUnit4-unit-testing-tutorial/?self)
- [《JUnit assertThat 用法》](http://www.iocoder.cn/Fight/JUnit-assertThat-usage/?self)

艿艿在如下两篇文章，也写了单元测试和集成测试的内容：

- [《芋道 Spring Boot SpringMVC 入门》](http://www.iocoder.cn/Spring-Boot/SpringMVC/?self)的[「3. 测试接口」](https://www.iocoder.cn/Spring-Boot/Unit-Test/?yudao#)小节，多提供了**集成**测试的示例。
- [《芋道 Spring Boot 响应式 WebFlux 入门》](http://www.iocoder.cn/Spring-Boot/WebFlux/?self)的[「3. 测试接口」](https://www.iocoder.cn/Spring-Boot/Unit-Test/?yudao#)小节，提供了 Webflux 的单元测试和集成测试的示例。

另外，也推荐如下的 Spring Boot Test 相关的文章：

- [《springboot test》](http://www.iocoder.cn/Fight/72b19e24a602/?self)
- [《Spring Boot 中文文档 —— 中文文档》](https://www.docs4dev.com/docs/zh/spring-boot/2.1.1.RELEASE/reference/boot-features-testing.html)