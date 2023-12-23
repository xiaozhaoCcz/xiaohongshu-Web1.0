package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class Comment extends BaseEntity {

    private String nid;

    private String uid;

    private String pid;

    private String replyId;

    private String replyUid;

    private Integer level;

    private Integer sort;

    private String content;

    private Long likeCount;

    private Long twoCommentCount;
}
