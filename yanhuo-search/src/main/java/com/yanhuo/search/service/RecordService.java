package com.yanhuo.search.service;

import com.yanhuo.xo.vo.RecordSearchVo;

import java.util.List;

public interface RecordService {
    List<RecordSearchVo> getRecordByKeyWord(String keyword);

    List<RecordSearchVo> getHotRecord();

    void addRecord(String keyword);
}
