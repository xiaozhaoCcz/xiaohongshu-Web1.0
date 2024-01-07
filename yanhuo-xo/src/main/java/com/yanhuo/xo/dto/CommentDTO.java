package com.yanhuo.xo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {

    private String nid;

    private String pid;

    private String replyId;

    private String replyUid;

    private Integer level;

    private String content;

}
