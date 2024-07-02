package cn.iocoder.educate.framework.elasticsearch.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * TODO j-sentinel 这里为了测试方便，我就先给了一些配置变量，后续建议删除！
 *
 * @author j-sentinel
 * @date 2024/3/5 15:34
 */
@ConfigurationProperties("lanxin.elasticsearch.rest")
@Data
@Validated
public class ElasticsearchProperty {

    /**
     * 是否开启
     */
    private Boolean enable = true;

    private String uris = "120.46.160.55";

    private Integer port = 9200;

    private String username = "elastic";

    private String password = "dp899556677";

    private String charSet = "UTF-8";

}
