package com.yanhuo.platform.client;

import com.yanhuo.platform.config.FeignRequestInterceptor;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author xiaozhao
 */
@FeignClient(value = "search",url = "http://localhost:8804",configuration = {FeignRequestInterceptor.class})
@Component
public interface EsClient {

    @RequestMapping("/search/note/test")
    void test();

    /**
     * 增加一条笔记
     *
     * @param noteSearchVo 笔记实体
     */
    @RequestMapping("/search/note/addNote")
    void addNote(@RequestBody NoteSearchVo noteSearchVo);

    /**
     * 修改一条笔记
     *
     * @param noteSearchVo 笔记实体
     */
    @RequestMapping("/search/note/updateNote")
    void updateNote(@RequestBody NoteSearchVo noteSearchVo);

    /**
     * 批量增加笔记
     *
     * @param noteSearchVoList 笔记集合
     */
    @RequestMapping("/search/note/addNoteBulkData")
    void addNoteBulkData(@RequestBody List<NoteSearchVo> noteSearchVoList);

    /**
     * 删除笔记
     *
     * @param noteId
     */
    @RequestMapping("/search/note/deleteNote/{noteId}")
    void deleteNote(@PathVariable String noteId);
}
