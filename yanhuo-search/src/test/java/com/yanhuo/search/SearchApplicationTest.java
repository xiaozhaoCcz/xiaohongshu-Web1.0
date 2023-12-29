package com.yanhuo.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.yanhuo.search.config.ESConfig;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = SearchApplication.class)
public class SearchApplicationTest {

    @Autowired
    ESConfig esConfig;
    @Autowired
    ElasticsearchClient elasticsearchClient;


    @Test
    public void test() throws IOException {
        // 创建索引
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c.index("user_test"));
        // 响应状态
        Boolean acknowledged = createIndexResponse.acknowledged();
        System.out.println("索引操作 = " + acknowledged);

        // 关闭ES客户端
        esConfig.close();
    }
}
