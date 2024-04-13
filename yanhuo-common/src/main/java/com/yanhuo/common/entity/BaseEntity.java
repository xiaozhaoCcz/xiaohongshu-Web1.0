package com.yanhuo.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaozhao
 */
@ApiModel("基础实体类")
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("id")
    @TableId
    private String id;

    @ApiModelProperty("创建用户")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    @ApiModelProperty("修改用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
