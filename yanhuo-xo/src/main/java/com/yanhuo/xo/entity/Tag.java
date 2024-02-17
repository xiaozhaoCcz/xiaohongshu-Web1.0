package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author xiaozhao
 */
@Data
@TableName("t_tag")
public class Tag extends BaseEntity {

    private Long likeCount;

    private String title;

    private Integer sort;
}
