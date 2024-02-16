package com.yanhuo.xo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
@ApiModel(value = "点赞收藏DTO")
public class LikeOrCollectionDTO implements Serializable {

    @ApiModelProperty("点赞或收藏的id")
    private String likeOrCollectionId;

    @ApiModelProperty("收藏收藏需要通知的用户id")
    private String publishUid;

    @ApiModelProperty("收藏收藏类型:1 点赞图片 2点赞评论  3收藏图片 4收藏专辑")
    private Integer type;
}
