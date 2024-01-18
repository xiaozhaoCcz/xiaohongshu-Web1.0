package com.yanhuo.im.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sendUid;

    private String acceptUid;

    private T content;

    private Long timestamp;

    private Integer msgType;

    private Integer chatType;
}
