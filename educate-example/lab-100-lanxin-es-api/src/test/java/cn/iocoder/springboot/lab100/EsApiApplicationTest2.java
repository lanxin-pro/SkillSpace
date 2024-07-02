package cn.iocoder.springboot.lab100;

import ch.qos.logback.core.util.TimeUtil;
import cn.iocoder.springboot.lab100.pojo.User;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author j-sentinel
 * @date 2024/2/15 14:37
 */
@Slf4j
@SpringBootTest
public class EsApiApplicationTest2 {

    @Resource
    private ElasticsearchClient esClient;

    /**
     * 测试添加索引
     *
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        esClient.indices().create(c -> c
                .index("java_test")
        );
    }

    /**
     * 添加文档
     *
     * @throws IOException
     */
    @Test
    public void testCreateDocument() throws IOException {
        User user = new User("王五", 21);
        IndexResponse response = esClient.index(i -> i
                .index("java_test")
                .id("2")
                .document(user)
        );
        log.info("Indexed with version " + response.version());
    }

    /**
     * 获取文档
     *
     * @throws IOException
     */
    @Test
    public void testGetDocument() throws IOException {
        GetResponse<User> response = esClient.get(g -> g
                        .index("java_test")
                .id("UtsasI0BsjvHo9h1qIOo"),
                User.class
        );
        if (response.found()) {
            User user = response.source();
            log.info("User name " + user.getName());
            log.info("User age " + user.getAge());
        } else {
            log.info ("User not found");
        }
    }

    @Test
    public void testWhereSearch() throws IOException {
        SearchResponse<User> response = esClient.search(s -> s
                        .index("java_test")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query("三张")
                                )
                        ),
                User.class
        );
    }

    @Test
    public void testUpdate() throws IOException {
        User user = new User("张三王五", 20);
        esClient.update(u -> u
                        .index("java_test")
                        .id("UtsasI0BsjvHo9h1qIOo")
                        .upsert(user),
                User.class
        );
    }

    @Test
    public void testDelete() throws IOException {
        DeleteResponse delete = esClient.delete(d -> d.index("java_test").id("2")
        );
        System.out.println(delete.result());
    }

    @Test
    public void testBulkRequest() throws IOException {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("蓝1", 20));
        users.add(new User("蓝2", 22));
        users.add(new User("蓝3", 23));
        users.add(new User("蓝4", 23));
        users.add(new User("蓝5", 20));
        users.add(new User("欣1", 15));
        users.add(new User("欣2", 16));
        users.add(new User("欣3", 19));
        BulkRequest.Builder br = new BulkRequest.Builder();

        IntStream.range(0, users.size()).forEach(i -> {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("java_test")
                            .id("" + (i + 1))
                            .document(users.get(i)))
            );
        });
        BulkResponse result = esClient.bulk(br.build());
        System.out.println(result);
    }

    @Test
    public void testBulkSearch() throws IOException {
        SearchResponse<User> search = esClient.search(s -> s
                        .index("java_test")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query("三张")
                                )
                        ),
                User.class
        );
        System.out.println(search.hits());
        search.hits().hits().forEach(doc -> {
            System.out.println(doc.source());
        });
    }

    @Test
    public void testBulkSearchHitLight() throws IOException {
        SearchResponse<User> search = esClient.search(s -> s
                        .index("java_test")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query("蓝")
                                )
                        ),
                User.class
        );
        System.out.println(search.hits());
        search.hits().hits().forEach(doc -> {
            System.out.println(doc.source());
        });
    }

}
