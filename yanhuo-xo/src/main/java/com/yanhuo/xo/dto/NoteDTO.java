package com.yanhuo.xo.dto;

import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class NoteDTO implements Serializable {

    @NotBlank(message = "标题不能为空", groups = AddGroup.class)
    private String title;

    @NotBlank(message = "内容不能为空", groups = AddGroup.class)
    private String content;

    private String noteCover;

    @NotBlank(message = "一级不能为空", groups = AddGroup.class)
    private String cid;

    @NotBlank(message = "二级不能为空", groups = AddGroup.class)
    private String cpid;

    private List<String> urls;

    private List<String> tagList;

    private Integer count;

    private Integer type;

}
