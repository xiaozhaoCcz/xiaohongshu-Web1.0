package com.yanhuo.platform.client;

import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "search",url = "http://localhost:8804")
@Component
public interface EsClient {

    @RequestMapping("/search/note/test")
    void test() ;

    @RequestMapping("/search/note/addNote")
    void addNote(@RequestBody NoteSearchVo noteSearchVo);

    @RequestMapping("/search/note/addNoteBulkData")
    void addNoteBulkData(@RequestBody List<NoteSearchVo> noteSearchVoList);
}
