package com.yanhuo.search.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.myVaildator.noLogin.NoLoginIntercept;
import com.yanhuo.search.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhao
 */
@RequestMapping("/record")
@RestController
public class RecordController {

    @Autowired
    RecordService recordService;

    /**
     * 获取搜索记录
     *
     * @param keyword 关键词
     * @return 记录集合
     */
    @GetMapping("getRecordByKeyWord")
    public Result<?> getRecordByKeyWord(String keyword) {
        return Result.ok(recordService.getRecordByKeyWord(keyword));
    }

    /**
     * 热门关键词
     *
     * @return list
     */
    @NoLoginIntercept
    @GetMapping("getHotRecord")
    public Result<?> getHotRecord() {
        return Result.ok(recordService.getHotRecord());
    }

    /**
     * 增加搜索记录
     *
     * @param keyword 关键词
     * @return success
     */
    @GetMapping("addRecord")
    public Result<?> addRecord(String keyword) {
        recordService.addRecord(keyword);
        return Result.ok();
    }
}
