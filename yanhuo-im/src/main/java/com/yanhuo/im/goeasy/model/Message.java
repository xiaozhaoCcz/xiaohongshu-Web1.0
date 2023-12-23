package com.yanhuo.im.goeasy.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class Message implements Serializable {

    private String appkey;

    private String senderId;

    private User to;

    private Payload payload;

    private String type;

    private Notification notifitation;

}
