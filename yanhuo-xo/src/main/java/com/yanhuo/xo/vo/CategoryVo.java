package com.yanhuo.xo.vo;

import com.yanhuo.common.utils.TreeNode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class CategoryVo extends TreeNode<CategoryVo> implements Serializable {
    private String title;
    private long likeCount;
}
