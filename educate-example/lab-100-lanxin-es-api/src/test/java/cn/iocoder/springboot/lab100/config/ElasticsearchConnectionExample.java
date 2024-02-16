///*
//package cn.iocoder.springboot.lab100.config;
//
//import org.apache.http.Header;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.Response;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Base64;
//
//@SpringBootTest
//public class ElasticsearchConnectionExample {
//
//    @Test
//    public void a() {
//        // 指定Elasticsearch集群的主机和端口
//        String hostname = "120.46.160.55";
//        int port = 9200;
//
//        // 用户名和密码
//        String username = "elastic";
//        String password = "dp899556677";
//        BasicHeader authorization = new BasicHeader("Authorization",
//                "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
//        );
//        // 创建RestHighLevelClient对象
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(new HttpHost(hostname, port, "http"))
//                        .setHttpClientConfigCallback(httpClientBuilder ->
//                                httpClientBuilder.setDefaultHeaders(Arrays.asList(new Header[]{authorization}))
//                        )
//        );
//
//        // 创建请求选项
//        RequestOptions requestOptions = RequestOptions.DEFAULT;
//
//        try {
//            // 发送PING请求来测试连接是否成功
//            CreateIndexRequest java_index = new CreateIndexRequest("java_index");
//            client.indices().create(java_index, RequestOptions.DEFAULT);
//            // 打印响应状态码
//            System.out.println("Connected to Elasticsearch cluster");
//
//        } catch (Exception e) {
//            // 处理连接失败的情况
//            System.err.println("Could not connect to Elasticsearch: " + e.getMessage());
//        } finally {
//            // 关闭客户端连接
//            try {
//                client.close();
//            } catch (IOException e) {
//                System.err.println("Error closing Elasticsearch client: " + e.getMessage());
//            }
//        }
//    }
//}
//*/
