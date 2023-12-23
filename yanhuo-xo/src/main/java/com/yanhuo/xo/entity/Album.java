package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
public class Album extends BaseEntity {

    private String title;

    private String uid;

    private String albumCover;

    private Integer sort;

    private Long imgCount;

    private Long collectionCount;

}
