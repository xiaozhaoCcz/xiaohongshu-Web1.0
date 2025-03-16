package com.yanhuo.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.service.NoteService;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiaozhao
 */
@Service
@Slf4j
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {

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
                        .should(h -> h.match(f -> f.field("title").boost(1f).query(noteDTO.getKeyword())))
                        .should(h -> h.match(f -> f.field("username").boost(0.5f).query(noteDTO.getKeyword())))
                        .should(h -> h.match(f -> f.field("content").boost(1f).query(noteDTO.getKeyword())))
                        .should(h -> h.match(f -> f.field("tags").boost(4f).query(noteDTO.getKeyword())))
                        .should(h -> h.match(f -> f.field("categoryName").boost(2f).query(noteDTO.getKeyword())))
                        .should(h -> h.match(f -> f.field("categoryParentName").boost(1.5f).query(noteDTO.getKeyword())))
                ));
            }
            if (StringUtils.isNotBlank(noteDTO.getCpid()) && StringUtils.isNotBlank(noteDTO.getCid())) {
                builder.query(q -> q.bool(b -> b
                        .must(h -> h.match(m -> m.field("cid").query(noteDTO.getCid())))
                        .must(h -> h.match(m -> m.field("cpid").query(noteDTO.getCpid())))
                ));
            } else if (StringUtils.isNotBlank(noteDTO.getCpid())) {
                builder.query(h -> h.match(m -> m.field("cpid").query(noteDTO.getCpid())));
            }

            if (noteDTO.getType() == 1) {
                builder.sort(o -> o.field(f -> f.field("likeCount").order(SortOrder.Desc)));
            } else if (noteDTO.getType() == 2) {
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


    @Override
    public Page<NoteSearchVo> getRecommendNotePage(long currentPage, long pageSize) {
        Page<NoteSearchVo> page = new Page<>();
        List<NoteSearchVo> noteSearchVoList = new ArrayList<>();
        //得到当前用户的浏览记录
        try {
            SearchRequest.Builder builder = new SearchRequest.Builder().index(NoteConstant.NOTE_INDEX);
            builder.size(1000);
            SearchRequest searchRequest = builder.build();
            SearchResponse<NoteSearchVo> searchResponse = elasticsearchClient.search(searchRequest, NoteSearchVo.class);
            TotalHits totalHits = searchResponse.hits().total();
            //得到所有的数据
            List<Hit<NoteSearchVo>> hits = searchResponse.hits().hits();
            for (Hit<NoteSearchVo> hit : hits) {
                NoteSearchVo noteSearchVo = hit.source();
                noteSearchVoList.add(noteSearchVo);
            }
            Collections.shuffle(noteSearchVoList);
            List<List<NoteSearchVo>> partition = Lists.partition(noteSearchVoList, (int) pageSize);
            List<NoteSearchVo> noteSearchVos = partition.get((int) currentPage - 1);
            page.setTotal(totalHits.value());
            page.setRecords(noteSearchVos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public void addNote(NoteSearchVo noteSearchVo) {
        try {
            CreateResponse createResponse = elasticsearchClient.create(e -> e.index(NoteConstant.NOTE_INDEX).id(noteSearchVo.getId()).document(noteSearchVo));
            log.info("createResponse.result{}", createResponse.result());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateNote(NoteSearchVo noteSearchVo) {
        try {
            UpdateResponse<NoteSearchVo> updateResponse = elasticsearchClient.update(e -> e.index(NoteConstant.NOTE_INDEX).id(noteSearchVo.getId()).doc(noteSearchVo), NoteSearchVo.class);
            log.info("updateResponse.result() = " + updateResponse.result());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNoteBulkData(List<NoteSearchVo> noteSearchVoList) {
        try {
            List<BulkOperation> result = new ArrayList<>();
            for (NoteSearchVo noteSearchVo : noteSearchVoList) {
                result.add(new BulkOperation.Builder().create(
                        d -> d.document(noteSearchVo).id(noteSearchVo.getId()).index(NoteConstant.NOTE_INDEX)).build());
            }
            BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index(NoteConstant.NOTE_INDEX).operations(result));
            log.info("createResponse.result{}", bulkResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNote(String noteId) {
        try {
            DeleteResponse deleteResponse = elasticsearchClient.delete(e -> e.index(NoteConstant.NOTE_INDEX).id(noteId));
            log.info("deleteResponse.result() ={} ", deleteResponse.result());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
