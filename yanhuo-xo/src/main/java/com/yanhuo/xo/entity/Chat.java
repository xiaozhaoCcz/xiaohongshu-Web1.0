package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_chat")
public class Chat extends BaseEntity {

    private String sendUid;

    private String acceptUid;

    private String content;

    // 0是私聊，1是群聊
    private Integer chatType;

    // 0通知消息，1是文本消息，2是图片消息，3是语音消息，4是视频消息，5 自定义消息
    private Integer msgType;

    private long timestamp;
}
