package com.yanhuo.search.utils.model;

import lombok.Data;

/**
 * @author zhaowenpeng
 */
@Data
public class FieldParam {

    private String field;

    private Float boost;

    private String value;
}
