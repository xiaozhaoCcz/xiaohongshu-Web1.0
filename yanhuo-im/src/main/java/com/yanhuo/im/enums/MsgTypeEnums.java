package com.yanhuo.im.enums;

import lombok.Getter;

@Getter
public enum MsgTypeEnums {

    /**
     *
     */
    TEXT(1, "text"),
    IMAGE(2, "image"),
    AUDIO(3, "audio"),
    VIDEO(4, "video");

    private final Integer type;

    private final String name;


    MsgTypeEnums(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName(Integer type){
        for (MsgTypeEnums item:MsgTypeEnums.values()) {
            if(item.getType().equals(type)){
                return item.getName();
            }
        }
        return null;
    }
}
