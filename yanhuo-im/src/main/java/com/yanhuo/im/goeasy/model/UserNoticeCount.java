package com.yanhuo.im.goeasy.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class UserNoticeCount implements Serializable {

    String uid;

    Integer notifyLikeOrCollectionCount;

    Integer notifyFanCount;

    Integer notifyCommentCount;

}
