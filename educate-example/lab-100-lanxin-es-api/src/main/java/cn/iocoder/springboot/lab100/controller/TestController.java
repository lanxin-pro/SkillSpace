//package cn.iocoder.springboot.lab100.controller;
//
//import org.apache.http.Header;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.ssl.TrustStrategy;
//import org.elasticsearch.client.*;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.net.ssl.SSLContext;
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
///**
// * @author j-sentinel
// * @date 2024/2/15 16:27
// */
//@RestController
//public class TestController {
//
//    @GetMapping("/test1")
//    public void t1() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        String a = "http://120.46.160.55:9200";
//        RestClientBuilder restClientBuilder = RestClient.builder(a);
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("120.46.160.55", 9200, "http")));
//        CreateIndexRequest request = new CreateIndexRequest("test_db");
//        client.indices().create(request, RequestOptions.DEFAULT);
//    }
//}
