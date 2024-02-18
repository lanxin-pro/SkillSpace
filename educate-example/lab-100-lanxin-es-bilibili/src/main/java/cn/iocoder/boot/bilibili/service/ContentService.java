package cn.iocoder.boot.bilibili.service;

import cn.iocoder.boot.bilibili.config.ElasticSearchClientConfig;
import cn.iocoder.boot.bilibili.pojo.Content;
import cn.iocoder.boot.bilibili.utils.HtmlParseUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlighterType;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author j-sentinel
 * @date 2024/2/16 20:11
 */
@Service
public class ContentService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // 1.解析数据 放入到 Es 索引中
    public Boolean parseContent(String keywords) throws IOException {
        ArrayList<Content> contents = HtmlParseUtil.extracted(keywords);
        BulkRequest.Builder builder = new BulkRequest.Builder();
        IntStream.range(0, contents.size()).forEach(i -> {
            builder.operations(op -> op
                    .index(idx -> idx
                            .index("java_test")
                            .id("" + (i + 1))
                            .document(contents.get(i))
                    ));
        });
        BulkResponse bulk = elasticsearchClient.bulk(builder.build());
        return !bulk.errors();
/*        for(int i = 0; i < contents.size(); i++) {

        }*/
    }


    public ArrayList<Content> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        if(pageNo <= 1) {
            pageNo = 1;
        }
        // 条件搜素
        int finalPageNo = pageNo;
        SearchResponse<Content> search = elasticsearchClient.search(s -> s
                        .index("java_test")
                        .from(finalPageNo)
                        .size(pageSize)
                        .query(q -> q
                                .match(t -> t
                                        .field("title")
                                        .query(keyword)
                                )
                        ).highlight(h -> h
                                .fields("title", highlight -> highlight)
                                .preTags("<span style='color:red'>")
                                .postTags("</span>")
                                .requireFieldMatch(true)
                        ),
                Content.class
        );
        // 查询 用户：蓝     -》》》》 1      蓝 1000
        ArrayList<Content> contents = new ArrayList<>();
        if(CollectionUtils.isEmpty(search.hits().hits())) {
            parseContent(keyword);
        }
        search.hits().hits().forEach(document -> {
            Map<String, List<String>> highlight = document.highlight();
            List<String> highlightList = highlight.get("title");
            contents.add(document.source());
            if(!CollectionUtils.isEmpty(highlightList)) {
                Content source = document.source();
                highlightList.forEach(hl -> source.setTitle(hl));
            }
        });
        return contents;
    }

}