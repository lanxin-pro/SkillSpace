//package cn.iocoder.springboot.lab100;
//
//import cn.iocoder.springboot.lab100.pojo.User;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import net.minidev.json.JSONUtil;
//import org.apache.http.HttpHeaders;
//import org.apache.http.HttpHost;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.ElasticsearchClient;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Timeout;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @author j-sentinel
// * @date 2024/2/15 14:37
// */
//@SpringBootTest
//public class EsApiApplicationTest {
//
//    @Resource
//    private RestHighLevelClient restHighLevelClient;
//
//    /**
//     * 测试文档
//     *
//     * @throws IOException
//     */
//    @Test
//    public void testCreateIndex() throws IOException {
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest("java_index");
//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("java_index");
//        restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//    }
//
//    /**
//     * 添加文档
//     *
//     * @throws IOException
//     */
//    @Test
//    public void testCreateDocument() throws IOException {
//        IndexRequest request = new IndexRequest("java_index");
//        request.source(JSONObject.toJSONString(Map.of("age", 20, "name", "李四")), XContentType.JSON);
//        restHighLevelClient.index(request, RequestOptions.DEFAULT);
//
//    }
//
//}
