package com.yanhuo.search.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.search.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhao
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;


    public Result<?> getNoteListByQueryParams(){
        return null;
    }
}
