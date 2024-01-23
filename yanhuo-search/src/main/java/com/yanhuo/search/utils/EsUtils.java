package com.yanhuo.search.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.search.utils.model.ConditionParam;
import com.yanhuo.search.utils.model.FieldParam;
import com.yanhuo.search.utils.model.OrderParam;
import com.yanhuo.search.utils.model.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaozhao
 * ElasticSearch 工具类
 */
@Component
@Slf4j
public class EsUtils {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public boolean isExistIndex(String index) {
        try {
            BooleanResponse exists = elasticsearchClient.indices().exists(e -> e.index(index));
            return exists.value();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加索引
     *
     * @param index
     * @return
     */
    public boolean addIndex(String index) {
        try {
            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c.index(index));
            return Boolean.TRUE.equals(createIndexResponse.acknowledged());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) {
        try {
            DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(e -> e.index(index));
            return Boolean.TRUE.equals(deleteIndexResponse.acknowledged());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> T queryDocument(String index,String id,Class<T> t) {
        GetResponse<T> getResponse = null;
        try {
            // 构建请求
            getResponse  = elasticsearchClient.get(e -> e.index(index).id(id), t);

        }catch (Exception e){
            e.printStackTrace();
        }
        return getResponse.source();
    }

    /**
     * 增加文档
     *
     * @param
     * @return
     */
    public <T> void addDocument(String index, T t) {
        try {
            Class<T> aClass = (Class<T>) t.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (name.equals("id")) {
                    String id = (String) field.get(t);
                    elasticsearchClient.create(e -> e.index(index).id(id).document(t));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文档
     *
     * @param index
     * @param t
     * @param <T>
     */
    public <T> void updateDocument(String index, T t) {
        try {
            Class<T> aClass = (Class<T>) t.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (name.equals("id")) {
                    String id = (String) field.get(t);
                    elasticsearchClient.update(e -> e.index(index).id(id).doc(t), aClass);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDocument(String index, String id) {
        try {
            elasticsearchClient.delete(e -> e.index(index).id(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void addBulkDocument(String index, List<T> list) {
        List<BulkOperation> bulkOperations = new ArrayList<>();
        try {
            for (T t : list) {
                Class<T> aClass = (Class<T>) t.getClass();
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    if (name.equals("id")) {
                        String id = (String) field.get(t);
                        bulkOperations.add(new BulkOperation.Builder().create(
                                d -> d.document(t).id(id).index(index)).build());
                        break;
                    }
                }
            }
            elasticsearchClient.bulk(e -> e.index(index).operations(bulkOperations));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBulkDocument(String index, List<String> ids) {
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        try {
            ids.forEach(item -> {
                list.add(new BulkOperation.Builder().delete(
                        d -> d.id(item).index(index)).build());
            });
            // 调用bulk方法执行批量插入操作
            elasticsearchClient.bulk(e -> e.index(index).operations(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> queryAllDocument(String index,Class<T> t){
        List<T> result = new ArrayList<>();
        try {
            SearchResponse<T> searchResponse = elasticsearchClient.search(e -> e.index(index).query(q -> q.matchAll(m -> m)), t);
            HitsMetadata<T> hits = searchResponse.hits();
            for (Hit<T> hit : hits.hits()) {
                result.add(hit.source());
            }
        }catch (Exception e) {e.printStackTrace();}
        return result;
    }


    /**
     * 根据条件查询数据
     */
    public <T> Page<T> getDocumentByCondition(SearchParam searchParam,Class<T> t){
        Page<T> page = new Page<>();
        List<T> result = new ArrayList<>();
        try {
            SearchRequest.Builder builder = new SearchRequest.Builder().index(searchParam.getIndex());
            List<FieldParam> fieldParamList = searchParam.getFieldParamList();
            if(fieldParamList!=null && !fieldParamList.isEmpty()){
                BoolQuery.Builder boolQuery = new BoolQuery.Builder();
                for (FieldParam fieldParam : fieldParamList) {
                    if(StringUtils.isNotEmpty(fieldParam.getValue())){
                        boolQuery.should(h->h.match(f->f.field(fieldParam.getField()).boost(fieldParam.getBoost()).query(fieldParam.getValue())));
                    }
                }
                builder.query(q->q.bool(boolQuery.build()));
            }
            List<ConditionParam> conditionParamList = searchParam.getConditionParamList();
            if(conditionParamList!=null && !conditionParamList.isEmpty()){
                BoolQuery.Builder boolQuery = new BoolQuery.Builder();
                for (ConditionParam conditionParam : conditionParamList) {
                    if(StringUtils.isNotEmpty(conditionParam.getValue())){
                        boolQuery.must(h->h.match(f->f.field(conditionParam.getField()).query(conditionParam.getValue())));
                    }
                }
            }
            OrderParam orderParam = searchParam.getOrderParam();
            if(orderParam!=null){
                builder.sort(o->o.field(f->f.field(orderParam.getField()).order(orderParam.getOrderType()==1? SortOrder.Desc:SortOrder.Asc)));
            }
            if(searchParam.getCurrentPage()!=null&&searchParam.getPageSize()!=null){
                builder.from((searchParam.getCurrentPage() - 1) * searchParam.getPageSize());
                builder.size(searchParam.getPageSize());
            }
            SearchRequest searchRequest = builder.build();
            SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, t);
            TotalHits totalHits = searchResponse.hits().total();
            page.setTotal(totalHits.value());
            List<Hit<T>> hits = searchResponse.hits().hits();
            for (Hit<T> hit : hits) {
                T data = hit.source();
                result.add(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        page.setRecords(result);
        return page;
    }
}
