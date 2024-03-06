package cn.iocoder.educate.framework.elasticsearch.config;

import cn.iocoder.educate.framework.elasticsearch.core.properties.ElasticsearchProperty;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Base64;

/**
 * 这里的身份验证没有使用证书 or 令牌的方式，我直接使用的是密码
 * 当然在es8当中非常推荐使用令牌的方式
 *
 * @author j-sentinel
 * @date 2024/3/5 15:15
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "lanxin.elasticsearch.rest", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(ElasticsearchProperty.class)
public class EducateElasticsearchConfiguration {

    private static final String BASIC_HEADER_NAME = "Authorization";

    private static final String BASIC_HEADER_VALUE = "Basic ";

    @Resource
    private ElasticsearchProperty elasticsearchProperty;

    @Bean
    public ElasticsearchClient restHighLevelClient(){
        // 指定Elasticsearch集群的主机和端口
        String hostname = elasticsearchProperty.getUris();
        int port = elasticsearchProperty.getPort();

        // 用户名和密码
        String username = elasticsearchProperty.getUsername();
        String password = elasticsearchProperty.getPassword();
        BasicHeader authorization = new BasicHeader(BASIC_HEADER_NAME,
                BASIC_HEADER_VALUE + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
        );

        Header[] defaultHeaders = new Header[]{
                new BasicHeader("Accept", "*/*"),
                new BasicHeader("Charset", elasticsearchProperty.getCharSet())
        };
        // 创建RestHighLevelClient对象
        RestClient restClient = RestClient.builder(new HttpHost(hostname, port, "http"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultHeaders(Arrays.asList(new Header[]{authorization}))
                )
                .setDefaultHeaders(defaultHeaders)
                .setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(Node node) {
                        log.error("监听某个es节点失败");
                    }
                })
                .build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        return new ElasticsearchClient(transport);
    }

}
