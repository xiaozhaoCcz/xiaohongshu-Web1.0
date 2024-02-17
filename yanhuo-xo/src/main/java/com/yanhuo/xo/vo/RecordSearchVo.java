package com.yanhuo.xo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class RecordSearchVo implements Serializable {

    private String content;

    private String highlightContent;

    private Long searchCount;
}
