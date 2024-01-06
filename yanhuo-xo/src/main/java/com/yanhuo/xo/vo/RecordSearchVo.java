package com.yanhuo.xo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class RecordSearchVo implements Serializable {

    private String content;

    private Long searchCount;
}
