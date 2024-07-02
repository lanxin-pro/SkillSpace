package cn.iocoder.educate.framework.ai.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ai 自动配置
 *
 * @author j-sentinel
 * @date 2024/7/2 10:31
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "educate.ai")
public class EducateAiProperties {


}
