package com.yanhuo.search.service.impl;

import cn.hutool.core.util.RandomUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.service.RecordService;
import com.yanhuo.xo.vo.RecordSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaozhao
 */
@Service
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Override
    public List<RecordSearchVo> getRecordByKeyWord(String keyword) {
        List<RecordSearchVo> records = new ArrayList<>();
        try {
            SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.RECOED_INDEX);
            if (StringUtils.isNotBlank(keyword)) {
                builder.query(q -> q.bool(b -> b
                        .should(h -> h.match(f -> f.field("content").query(keyword)))
                ));
            }
            builder.sort(o -> o.field(f -> f.field("searchCount").order(SortOrder.Desc)));
            builder.highlight(h -> h.fields("content", m -> m).preTags("<font color='black'>")
                    .postTags("</font>"));
            builder.size(10);
            SearchRequest searchRequest = builder.build();
            SearchResponse<RecordSearchVo> searchResponse = elasticsearchClient.search(searchRequest, RecordSearchVo.class);
            //得到所有的数据
            List<Hit<RecordSearchVo>> hits = searchResponse.hits().hits();

            // 高亮查询
            for (Hit<RecordSearchVo> hit : hits) {
                Map<String, List<String>> highlight = hit.highlight();
                String content = highlight.get("content").get(0);
                RecordSearchVo recordSearchVo = hit.source();
                recordSearchVo.setHighlightContent(content);
                records.add(recordSearchVo);
            }
            return records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RecordSearchVo> getHotRecord() {
        List<RecordSearchVo> records = new ArrayList<>();
        try {
            BooleanResponse exists = elasticsearchClient.indices().exists(e -> e
                    .index(NoteConstant.RECOED_INDEX));
            if (!exists.value()) {
                return records;
            }
            SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.RECOED_INDEX);
            builder.sort(o -> o.field(f -> f.field("searchCount").order(SortOrder.Desc)));
            builder.size(10);
            SearchRequest searchRequest = builder.build();
            SearchResponse<RecordSearchVo> searchResponse = elasticsearchClient.search(searchRequest, RecordSearchVo.class);
            //得到所有的数据
            List<Hit<RecordSearchVo>> hits = searchResponse.hits().hits();
            for (Hit<RecordSearchVo> hit : hits) {
                RecordSearchVo recordSearchVo = hit.source();
                records.add(recordSearchVo);
            }
            return records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRecord(String keyword) {
        try {
            // 查询索引是否存在

            BooleanResponse exists = elasticsearchClient.indices().exists(e -> e
                    .index(NoteConstant.RECOED_INDEX));
            if (!exists.value()) {
                elasticsearchClient.indices().create(c -> c.index(NoteConstant.RECOED_INDEX));
            }

            SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.RECOED_INDEX);
            if (StringUtils.isNotBlank(keyword)) {
                builder.query(q -> q.match(f -> f.field("content").query(keyword.trim())));
            }
            builder.size(10);
            SearchRequest searchRequest = builder.build();
            SearchResponse<RecordSearchVo> searchResponse = elasticsearchClient.search(searchRequest, RecordSearchVo.class);
            //得到所有的数据
            List<Hit<RecordSearchVo>> hits = searchResponse.hits().hits();

            List<String> contents = new ArrayList<>();
            // 高亮查询
            for (Hit<RecordSearchVo> hit : hits) {
                RecordSearchVo recordSearchVo = hit.source();
                recordSearchVo.setSearchCount(recordSearchVo.getSearchCount() + 1);
                UpdateResponse<RecordSearchVo> response = elasticsearchClient.update(u -> u.index(NoteConstant.RECOED_INDEX).id(hit.id()).doc(recordSearchVo), RecordSearchVo.class);
                log.info("response", response.toString());
                contents.add(recordSearchVo.getContent());
            }

            if (StringUtils.isNotBlank(keyword) && !contents.contains(keyword.trim())) {
                RecordSearchVo recordSearchVo = new RecordSearchVo();
                recordSearchVo.setContent(keyword);
                recordSearchVo.setSearchCount(1L);
                String id = RandomUtil.randomString(12);
                elasticsearchClient.create(c -> c.index(NoteConstant.RECOED_INDEX).id(id).document(recordSearchVo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
