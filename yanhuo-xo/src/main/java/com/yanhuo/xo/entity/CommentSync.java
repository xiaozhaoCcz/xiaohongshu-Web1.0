package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_comment_sync")
public class CommentSync extends BaseEntity {

    private String nid;

    private String noteUid;

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
