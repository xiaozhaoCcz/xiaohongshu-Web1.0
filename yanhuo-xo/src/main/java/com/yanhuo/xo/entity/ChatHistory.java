package com.yanhuo.xo.entity;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
public class ChatHistory extends BaseEntity {

    private String sendUid;

    private String acceptUid;

    private Long chatCount;

    private String content;

    private Integer type;

    //如果是图片和语音则显示地址
    private String url;
}
