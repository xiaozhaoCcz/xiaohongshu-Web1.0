package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author xiaozhao
 */
@Data
@Accessors(chain = true)
public class AlbumVo implements Serializable {

    /**
     *
     */
    private String id;
    /**
     *
     */
    private String title;

    /**
     *
     */
    private String albumCover;
    /**
     *
     */
    private Integer sort;

    /**
     * 图片数量
     */
    private Long imgCount;

    /**
     * 收藏数量
     */
    private Long collectionCount;

    private String userId;

    private String username;

    private String avatar;
}
