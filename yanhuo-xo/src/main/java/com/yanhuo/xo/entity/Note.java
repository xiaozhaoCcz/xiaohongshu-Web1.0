package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_note")
public class Note extends BaseEntity {

    private String title;

    private String content;

    private String noteCover;

    private Integer noteCoverHeight;

    private String uid;

    private String cid;

    private String cpid;

    private String urls;

    //图片数量
    private Integer count;

    private Integer sort;

    //是否置顶
    private Integer pinned;

    private Integer status;

    //类型（图片或视频）
    private Integer type;

    private Long likeCount;

    private Long collectionCount;

    private Long commentCount;

    private Long viewCount;

}
