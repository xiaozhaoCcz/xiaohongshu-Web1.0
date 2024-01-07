package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CommentVo implements Serializable {

    private String id;

    private String pid;

    private String uid;

    private String username;

    private String avatar;

    private String replyId;

    private String replyUid;

    private String content;

    private Long time;

    private Long likeCount;

    private Boolean isLike;

    private Long twoCommentCount;

    private List<CommentVo> children;

}
