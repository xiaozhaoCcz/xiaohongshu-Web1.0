package com.yanhuo.search.utils.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {

    private Integer currentPage;

    private Integer pageSize;

    private String index;

    private List<FieldParam> fieldParamList;

    private OrderParam orderParam;

    private List<ConditionParam> conditionParamList;

}
