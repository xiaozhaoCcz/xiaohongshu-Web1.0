package com.yanhuo.xo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NoteVo implements Serializable {

    private String id;

    private String content;

    private String noteCover;

    private String uid;

    private String cid;

    private String cPid;

    private String urls;

    //图片数量
    private Integer count;

    private Integer sort;

    //是否置顶
    private Integer pinned;

    private Integer status;

    //类型（图片或视频）
    private Integer type;

    private Long likeCount;

    private Long collectionCount;

    private Long commentCount;

    private Long viewCount;

}
