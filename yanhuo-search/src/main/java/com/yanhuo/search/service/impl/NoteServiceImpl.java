package com.yanhuo.search.service.impl;

import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.util.ObjectBuilder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.config.ESConfig;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.service.NoteService;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {

    @Autowired
    ESConfig esConfig;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Override
    public Page<NoteSearchVo> getNotePageByDTO(long currentPage, long pageSize, NoteDTO noteDTO) {

        Page<NoteSearchVo> page = new Page<>();

        List<NoteSearchVo> noteSearchVoList = new ArrayList<>();

        try {
            SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.NOTE_INDEX);

            if (StringUtils.isNotBlank(noteDTO.getKeyword())) {
                builder.query(q -> q.bool(b -> b
                        .should(h -> h.fuzzy(f -> f.field("content").value(noteDTO.getKeyword()).fuzziness("6")))
                        .should(h -> h.fuzzy(f -> f.field("username").value(noteDTO.getKeyword()).fuzziness("4")))
                ));
            }

            if (StringUtils.isNotBlank(noteDTO.getCid())) {
                builder.query(q -> q.match(m -> m.field("cid").query(noteDTO.getCid())));
            }

            if (noteDTO.getType() == 1) {
                builder.sort(o -> o.field(f -> f.field("likeCount").order(SortOrder.Desc)));
            } else {
                builder.sort(o -> o.field(f -> f.field("time").order(SortOrder.Desc)));
            }

            builder.from((int) (currentPage - 1) * (int) pageSize);
            builder.size((int) pageSize);

            SearchRequest searchRequest = builder.build();
            SearchResponse<NoteSearchVo> searchResponse = elasticsearchClient.search(searchRequest, NoteSearchVo.class);
            TotalHits totalHits = searchResponse.hits().total();
            page.setTotal(totalHits.value());
            List<Hit<NoteSearchVo>> hits = searchResponse.hits().hits();

            for (Hit<NoteSearchVo> hit : hits) {
                NoteSearchVo noteSearchVo = hit.source();
                noteSearchVoList.add(noteSearchVo);
            }
        } catch (Exception e) {
            throw new YanHuoException("es查找数据异常");
        }
        page.setRecords(noteSearchVoList);
        return page;
    }
}
