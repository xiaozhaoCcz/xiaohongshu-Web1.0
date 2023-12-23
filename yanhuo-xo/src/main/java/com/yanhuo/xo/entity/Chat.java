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

    private Long chatCount;

    private String content;

    private Integer chatType;

    private Integer msgType;

    private long timestamp;

    //如果是图片和语音则显示地址
    private String url;
}
