package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_album_note_relation")
public class AlbumNoteRelation extends BaseEntity {

    private String aid;

    //笔记
    private String nid;

}
