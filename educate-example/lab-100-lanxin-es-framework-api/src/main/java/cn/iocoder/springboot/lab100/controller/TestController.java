package cn.iocoder.springboot.lab100.controller;

import cn.iocoder.springboot.lab100.pojo.Content;
import cn.iocoder.springboot.lab100.service.TestService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author j-sentinel
 * @date 2024/2/15 16:27
 */
@RestController
public class TestController {

    /**
     * 注：这里会调用框架中的配置类
     */
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @GetMapping("/test1")
    public User page() throws IOException {
        GetResponse<User> userGetResponse = elasticsearchClient.get(g -> g
                        .index("java_test")
                        .id("UtsasI0BsjvHo9h1qIOo"),
                User.class
        );
        return userGetResponse.source();
    }

    @Test
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
