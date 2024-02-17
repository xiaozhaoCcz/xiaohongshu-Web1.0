package com.yanhuo.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaozhao
 */
@Data
@Accessors(chain = true)
public class TrendVo implements Serializable {

    private String nid;

    private String uid;

    private String username;

    private String avatar;

    private Long time;

    private String content;

    private List<String> imgUrls;

    private Long likeCount;

    private Long commentCount;

    private Boolean isLike;

    private Boolean isLoading;
}
