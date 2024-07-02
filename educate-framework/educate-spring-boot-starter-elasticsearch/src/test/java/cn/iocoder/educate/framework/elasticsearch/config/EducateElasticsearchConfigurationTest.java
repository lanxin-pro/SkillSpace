package cn.iocoder.educate.framework.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author j-sentinel
 * @date 2024/3/5 15:47
 */
@Slf4j
@SpringBootTest(classes = EducateElasticsearchConfiguration.class)
public class EducateElasticsearchConfigurationTest {

    @Resource
    private ApplicationContext context;

    @Test
    public void testB() throws IOException {
        // 获取@Configuration类中定义的Bean
        ElasticsearchClient yourBean = context.getBean(ElasticsearchClient.class);

        GetResponse<User> response = yourBean.get(g -> g
                        .index("java_test")
                        .id("UtsasI0BsjvHo9h1qIOo"),
                User.class
        );

        System.out.println(response);
    }

    @org.junit.Test
    public void a() throws IOException {
        // 指定Elasticsearch集群的主机和端口
        String hostname = "http://120.46.160.55:9200";
        int port = 9200;

        // 用户名和密码
        String username = "elastic";
        String password = "dp899556677";
        BasicHeader authorization = new BasicHeader("Authorization",
                "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
        );
        RestClient.builder(new HttpHost(hostname, port, "http"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultHeaders(Arrays.asList(new Header[]{authorization}))
                );
        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(hostname))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization",
                                "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
                        )
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        GetResponse<User> response = esClient.get(g -> g
                        .index("java_test")
                        .id("UtsasI0BsjvHo9h1qIOo"),
                User.class
        );

        System.out.println(response);
    }

}
