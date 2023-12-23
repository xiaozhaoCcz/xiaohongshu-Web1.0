package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

@Data
public class Category extends BaseEntity {

    private String title;

    private String pid;

    private String desc;

    private long likeCount;

    private Integer sort;

    private String normalCover;

    private String hotCover;

}
