package com.yanhuo.search.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.search.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/search/record")
@RestController
public class RecordController {

    @Autowired
    RecordService recordService;

    @GetMapping("getRecordByKeyWord")
    public Result<?> getRecordByKeyWord(String keyword){
        return Result.ok(recordService.getRecordByKeyWord(keyword));
    }
}
