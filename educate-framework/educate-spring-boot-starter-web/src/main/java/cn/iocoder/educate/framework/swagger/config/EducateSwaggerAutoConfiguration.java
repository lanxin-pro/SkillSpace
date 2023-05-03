package cn.iocoder.educate.framework.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/3 19:09
 * 理论而言我加不加@Configuration都是一模一样的
 */
@Configuration
@ConditionalOnClass({OpenAPI.class})
@EnableConfigurationProperties(SwaggerProperties.class)
public class EducateSwaggerAutoConfiguration {

    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
                .info(this.apiInfo());
    }

    /**
     * 创建 API 信息
     */
    private Info apiInfo() {
        return new Info()
                .title("测试接口文档示例")
                .description("我是一段描述")
                .version("1.0.0")
                .contact(new Contact().name("联系人"))
                .license(new License().name("许可证"));
    }
}
