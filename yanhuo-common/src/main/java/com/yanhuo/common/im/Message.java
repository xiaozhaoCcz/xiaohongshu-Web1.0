package com.yanhuo.common.im;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sendUid;

    private String acceptUid;

    private Object content;

    private Integer msgType;

    private Integer chatType;
}
