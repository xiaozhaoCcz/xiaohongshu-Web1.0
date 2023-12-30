package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NoteSearchVo  implements Serializable {

    private String id;

    private String content;

    private String noteCover;

    private String cid;

    private String cpid;

    private String uid;

    private String username;

    private String avatar;

    private String urls;

    //是否置顶
    private Integer pinned;

    private Integer status;

    private Long likeCount;

    private Long time;

}
