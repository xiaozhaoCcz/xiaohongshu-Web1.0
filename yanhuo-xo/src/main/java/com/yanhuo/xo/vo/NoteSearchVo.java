package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
@Accessors(chain = true)
public class NoteSearchVo  implements Serializable {

    private String id;

    private String title;

    private String content;

    private String noteCover;

    private Integer noteCoverHeight;

    private String cid;

    private String categoryName;

    private String cpid;

    private String categoryParentName;

    private String uid;

    private String username;

    private String avatar;

    private String urls;

    private String tags;

    //是否置顶
    private Integer pinned;

    private Integer status;

    private Long likeCount;

    private Long viewCount;

    private Long time;

    private Boolean isLoading;



}
