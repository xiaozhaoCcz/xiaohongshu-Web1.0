package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_chat_user_relation")
public class ChatUserRelation implements Serializable {

    private String id;

    private String sendUid;

    private String acceptUid;

    private String content;

    // 用户未读消息数量
    private Integer count;

    // 0是私聊，1是群聊
    private Integer chatType;

    // 0是文本消息，1是图片消息，2是语音消息，3是视频消息
    private Integer msgType;

    private long timestamp;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}
