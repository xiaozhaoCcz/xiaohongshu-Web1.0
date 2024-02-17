package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
@Accessors(chain = true)
public class LikeOrCollectionVo implements Serializable {

    private String itemId;

    private String itemCover;

    private String uid;

    private String username;

    private String avatar;

    private String content;

    private Long time;

    private Integer type;

}
