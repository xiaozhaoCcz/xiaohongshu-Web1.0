package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

@Data
public class Tag extends BaseEntity {

    private Long likeCount;

    private String title;

    private Integer sort;
}
