package cn.iocoder.springboot.lab100.config;

import cn.iocoder.springboot.lab100.pojo.User;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ElasticsearchConnectionExample2 {

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
