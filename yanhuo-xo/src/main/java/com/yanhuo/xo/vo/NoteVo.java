package com.yanhuo.xo.vo;

import com.yanhuo.xo.entity.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaozhao
 */
@Data
@Accessors(chain = true)
public class NoteVo implements Serializable {

    private String id;

    private String title;

    private String content;

    private String noteCover;

    private String uid;

    private String username;

    private String avatar;

    private String urls;

    private String cid;

    private String cpid;

    //图片数量
    private Integer count;

    //类型（图片或视频）
    private Integer type;

    private Long likeCount;

    private Long collectionCount;

    private Long commentCount;

    private List<Tag> tagList;

    private Long time;

    private Integer pinned;

    //点赞关注收藏
    private Boolean isFollow;

    private Boolean isLike;

    private Boolean isCollection;
}
