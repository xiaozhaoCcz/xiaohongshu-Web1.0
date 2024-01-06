package com.yanhuo.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.service.RecordService;
import com.yanhuo.xo.vo.RecordSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
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
            builder.highlight(h -> h.fields("content",m->m).preTags("<font color='black'>")
                    .postTags("</font>"));
            builder.size(10);
            SearchRequest searchRequest = builder.build();
            SearchResponse<RecordSearchVo> searchResponse = elasticsearchClient.search(searchRequest, RecordSearchVo.class);
            //得到所有的数据
            List<Hit<RecordSearchVo>> hits = searchResponse.hits().hits();

            // 高亮查询
            for (Hit<RecordSearchVo> hit : hits) {
                Map<String, List<String>> highlight = hit.highlight();
                String content =  highlight.get("content").get(0);
                RecordSearchVo recordSearchVo = hit.source();
                recordSearchVo.setContent(content);
                records.add(recordSearchVo);
            }
            return records;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
