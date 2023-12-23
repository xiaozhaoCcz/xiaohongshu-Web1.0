package com.yanhuo.im.goeasy.model;

import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
public abstract class Payload {
    // 消息id
    String mid;

    String content;

    Integer notifyLikeOrCollectionCount;

    Integer notifyFanCount;

    Integer notifyCommentCount;

}
