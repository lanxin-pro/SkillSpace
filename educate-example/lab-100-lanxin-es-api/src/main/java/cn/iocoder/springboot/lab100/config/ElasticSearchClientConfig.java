package cn.iocoder.springboot.lab100.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Base64;

/**
 * @author j-sentinel
 * @date 2024/2/15 14:29
 */
@Configuration
public class ElasticSearchClientConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        // 指定Elasticsearch集群的主机和端口
        String hostname = "120.46.160.55";
        int port = 9200;

        // 用户名和密码
        String username = "elastic";
        String password = "dp899556677";
        BasicHeader authorization = new BasicHeader("Authorization",
                "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
        );
        // 创建RestHighLevelClient对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(hostname, port, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder ->
                                httpClientBuilder.setDefaultHeaders(Arrays.asList(new Header[]{authorization}))
                        )
        );
        return client;
    }
}
