package com.yanhuo.xo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NoteDTO implements Serializable {

    private String title;

    private String content;

    private String noteCover;

    private String cid;

    private String cpid;

    private List<String> urls;

    private List<String> tagList;

    private Integer count;

    private Integer type;

}
