package com.yanhuo.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.config.ESConfig;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(classes = SearchApplication.class)
public class SearchApplicationTest {

    @Autowired
    ESConfig esConfig;
    @Autowired
    ElasticsearchClient elasticsearchClient;



    //创建索引
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

    @Test
    public void queryIndex() throws IOException {
        GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(e -> e.index("user_test"));
        System.out.println("getIndexResponse.result() = " + getIndexResponse.result());
        System.out.println("getIndexResponse.result().keySet() = " + getIndexResponse.result().keySet());
        // 关闭ES客户端
        esConfig.close();
    }
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(e -> e.index("user_test"));
        System.out.println("删除操作 = " + deleteIndexResponse.acknowledged());
        // 关闭ES客户端
        esConfig.close();
    }


//    @Test
//    public void addDocument() throws IOException {
//
//        // 向user对象中添加数据
//        UserTest user = new UserTest("java客户端", 1, 18);
//        // 向索引中添加数据
//        CreateResponse createResponse = elasticsearchClient.create(e -> e.index("user_test").id("1001").document(user));
//        System.out.println("createResponse.result() = " + createResponse.result());
//
//        // 关闭ES客户端
//        esConfig.close();
//    }


//    @Test
//    public void queryDocument() throws IOException {
//        // 构建请求
//        GetResponse<UserTest> getResponse = elasticsearchClient.get(e -> e.index("user_test").id("1001"), UserTest.class);
//        System.out.println("getResponse.source().toString() = " + getResponse.source().toString());
//        esConfig.close();
//    }
//
//
//    @Test
//    public void modifyDocument() throws IOException {
//        // 使用map集合封装需要修改的内容
//        UserTest user = new UserTest("java客户端aaaa", 1, 19);
//        user.setCreator("2222");
//        // 构建请求
//        UpdateResponse<UserTest> updateResponse = elasticsearchClient.update(e -> e.index("user_test").id("1001").doc(user), UserTest.class);
//        System.out.println("updateResponse.result() = " + updateResponse.result());
//
//        esConfig.close();
//    }
//
//
//
//    @Test
//    public void removeDocument() throws  IOException {
//        // 构建请求
//        DeleteResponse deleteResponse = elasticsearchClient.delete(e -> e.index("user_test").id("1001"));
//        System.out.println("deleteResponse.result() = " + deleteResponse.result());
//        esConfig.close();
//    }
//
//
//    @Test
//    public void batchAddDocument() throws IOException {
//
//
//        // 构建一个批量数据集合
//        List<BulkOperation> list = new ArrayList<>();
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test2", 1, 19)).id("1002").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test3", 1, 20)).id("1003").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test4", 0, 21)).id("1004").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test5", 1, 19)).id("1005").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test6", 1, 20)).id("1006").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test7", 0, 21)).id("1007").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test8", 0, 19)).id("1008").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("test9", 1, 20)).id("1009").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("aaa", 0, 21)).id("10010").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("aaaa", 1, 19)).id("10011").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("aaaaa", 1, 20)).id("10012").index("user_test")).build());
//        list.add(new BulkOperation.Builder().create(
//                d -> d.document(new UserTest("aaaaaab", 0, 21)).id("10013").index("user_test")).build());
//        // 调用bulk方法执行批量插入操作
//        BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index("user_test").operations(list));
//        System.out.println("bulkResponse.items() = " + bulkResponse.items());
//
//        esConfig.close();
//    }
//
//
//    @Test
//    public void batchDeleteDocument() throws IOException {
//        // 构建一个批量数据集合
//        List<BulkOperation> list = new ArrayList<>();
//        list.add(new BulkOperation.Builder().delete(
//                d -> d.id("1002").index("user_test")).build());
//        list.add(new BulkOperation.Builder().delete(
//                d -> d.id("1003").index("user_test")).build());
//        // 调用bulk方法执行批量插入操作
//        BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index("user_test").operations(list));
//        System.out.println("bulkResponse.items() = " + bulkResponse.items());
//
//        esConfig.close();
//    }
//
//
//    @Test
//    public void queryAllDocument() throws IOException {
//        // 全量查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(e -> e.index("user_test").query(q -> q.matchAll(m -> m)), UserTest.class);
//        HitsMetadata<UserTest> hits = searchResponse.hits();
//        for (Hit<UserTest> hit : hits.hits()) {
//            System.out.println("user = " + hit.source().toString());
//        }
//        System.out.println("searchResponse.hits().total().value() = " + searchResponse.hits().total().value());
//        esConfig.close();
//
//    }
//
//
//    @Test
//    public void pagingQueryDocument() throws IOException {
//        // 分页查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(
//                s -> s.index("user_test")
//                        .query(q -> q.matchAll(m -> m))
//                        .from(2)
//                        .size(2)
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//    }
//
//
//    @Test
//    public void sortQueryDocument() throws IOException{
//        // 排序查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(
//                s -> s.index("user_test")
//                        .query(q -> q.matchAll(m -> m))
//                        .sort(o -> o.field(f -> f.field("age").order(SortOrder.Asc)))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//
//        esConfig.close();
//    }
//
//    @Test
//    public void conditionQueryDocument() throws IOException {
//
//        // 条件查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(
//                s -> s.index("user_test").query(q -> q.matchAll(m -> m))
//                        .sort(o -> o.field(f -> f.field("age").order(SortOrder.Asc)))
//                        .source(r -> r.filter(f -> f.includes("name","age").excludes("")))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//    }
//
//    @Test
//    public void combinationQueryDocument() throws IOException {
//        // 组合查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(
//                s -> s.index("user_test").query(q -> q.bool(b -> b
//                        .must(m -> m.match(u -> u.field("age").query(20)))
//                        .must(m -> m.match(u -> u.field("sex").query(1)))
//                        .mustNot(m -> m.match(u -> u.field("sex").query(0)))
//                ))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//
//    }
//
//    @Test
//    public void combinationQueryDocument2() throws IOException {
//        // 组合查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(
//                s -> s.index("user_test").query(q -> q.bool(b -> b
//                        .should(h -> h.match(u -> u.field("age").query(19)))
//                        .should(h -> h.match(u -> u.field("sex").query(0)))
//                ))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//    }
//
//
//    @Test
//    public void scopeQueryDocument2() throws IOException {
//
//        // 范围查询，gte()表示取大于等于，gt()表示大于，lte()表示小于等于
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(s -> s.index("user_test").query(q -> q
//                        .range(r -> r.field("age").gte(JsonData.of(20)).lt(JsonData.of(21))))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//    }
//
//
//    @Test
//    public void fuzzyQueryDocument2() throws IOException {
//        // 模糊查询，fuzziness表示差几个可以查询出来
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(s -> s.index("user_test").query(q -> q
//                        .fuzzy(f -> f.field("name").value("tes").fuzziness("6")))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//        esConfig.close();
//
//    }
//
//
//    @Test
//    public void highlightQueryDocument2() throws IOException {
//
//        // 高亮查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(s -> s.index("user_test").query(q -> q
//                                .term(t -> t.field("name").value("test3")))
//                        .highlight(h -> h.fields("name", f -> f.preTags("<font color='red'>").postTags("</font>")))
//                , UserTest.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//
//        esConfig.close();
//    }
//
//
//    @Test
//    public void aggregateQueryDocument2() throws IOException {
//
//        // 聚合查询，取最大年龄
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(s -> s.index("user_test").aggregations("maxAge", a ->a.max(m -> m.field("age")))
//                , UserTest.class);
//        searchResponse.aggregations().entrySet().forEach(f -> System.out.println(f.getKey() + ":" + f.getValue().max().value()));
//
//        esConfig.close();
//    }
//
//
//    @Test
//    public void groupQueryDocument2() throws IOException {
//        // 分组查询
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(s -> s.index("user_test")
//                        .aggregations("ageGroup", a ->a.terms(t -> t.field("age")))
//                , UserTest.class);
//        searchResponse.aggregations().get("ageGroup").lterms().buckets().array().forEach(f -> System.out.println(f.key() + ":" + f.docCount()));
//        esConfig.close();
//    }
//
//
//
//    // -----------------------------------------------------------------------------
//
//    @Test
//    public void tt1() throws IOException {
//        SearchRequest.Builder builder = new SearchRequest.Builder().index("user_test");
//        builder.aggregations("sexAGG", a -> a.terms(t->t.field("sex")));
//        builder.aggregations("ageAGG", a -> a.terms(t->t.field("age")));
//        builder.query(q -> q
//                .fuzzy(f -> f.field("name").value("test1").fuzziness("6")));
//
//        builder.from(1);
//        builder.size(2);
//        SearchRequest build = builder.build();
//        SearchResponse<UserTest> searchResponse = elasticsearchClient.search(build,UserTest.class);
//        searchResponse.aggregations().get("sexAGG").lterms().buckets().array().forEach(f -> System.out.println(f.key() + ":" + f.docCount()));
//        searchResponse.aggregations().get("ageAGG").lterms().buckets().array().forEach(f -> System.out.println(f.key() + ":" + f.docCount()));
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//
//    }
//
//
//    // ----------------------高级聚合------------------
//
//    @Test
//    public void tt2() throws IOException {
//        SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.NOTE_INDEX);
//
////        SearchRequest build = builder.query(q -> q.bool(b -> b
////                .should(h -> h.match(u -> u.field("content").query("头像")))
////                .should(h -> h.match(u -> u.field("username").query("头像")))
////        )).build();
//        builder.query(q -> q.bool(b -> b
//                .should(h -> h.fuzzy(f -> f.field("content").value("头像").fuzziness("6")))
//                .should(h -> h.fuzzy(f -> f.field("username").value("头像").fuzziness("4")))
//        ));
//        builder.sort(o -> o.field(f -> f.field("time").order(SortOrder.Desc)));
//        builder.from(1);
//        builder.size(2);
//        SearchRequest build = builder.build();
//
//        SearchResponse<NoteSearchVo> searchResponse = elasticsearchClient.search(build,NoteSearchVo.class);
//        searchResponse.hits().hits().forEach(h -> System.out.println(h.source().toString()));
//
//    }
//
//
//    @Test
//    public void tt4() throws IOException {
//        // 构建一个批量数据集合
////        List<BulkOperation> list = new ArrayList<>();
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("aabb",20L)).id("1001").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("ccdd",19L)).id("1002").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("eeff",30L)).id("1003").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("aaff",40L)).id("1004").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("ddtt",70L)).id("1005").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("yyuu",10L)).id("1006").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("iijj",60L)).id("1007").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("rrgg",29L)).id("1008").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("aarr",24L)).id("1009").index(NoteConstant.RECOED_INDEX)).build());
////        list.add(new BulkOperation.Builder().create(
////                d -> d.document(new RecordSearchVo("eegg",21L)).id("10010").index(NoteConstant.RECOED_INDEX)).build());
////        // 调用bulk方法执行批量插入操作
////        BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index(NoteConstant.RECOED_INDEX).operations(list));
////        System.out.println("bulkResponse.items() = " + bulkResponse.items());
////
////        esConfig.close();
//    }
}
