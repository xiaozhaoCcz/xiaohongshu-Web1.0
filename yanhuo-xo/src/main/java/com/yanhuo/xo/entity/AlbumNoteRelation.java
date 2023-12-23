package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
public class AlbumNoteRelation extends BaseEntity {

    private String aid;

    // 笔记
    private String nid;

}
