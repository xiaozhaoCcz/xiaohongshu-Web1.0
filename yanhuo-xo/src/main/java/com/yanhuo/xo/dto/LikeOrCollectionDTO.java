package com.yanhuo.xo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeOrCollectionDTO implements Serializable {

    private String uid;

    private String likeOrCollectionId;

    private String publishUid;

    private Integer type;
}
