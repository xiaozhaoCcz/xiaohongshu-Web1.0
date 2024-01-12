package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_album")
public class Album extends BaseEntity {

    private String title;

    private String uid;

    private String albumCover;

    // 0默认专辑
    private Integer type;

    private Integer sort;

    private Long imgCount;

    private Long collectionCount;

}
