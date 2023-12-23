package com.yanhuo.im.enums;

import lombok.Getter;

/**
 * @author xiaozhao
 */
@Getter
public enum ChatTypeEnums {

    /**
     *
     */
    PRIVATE(1,"private"),
    GROUP(2,"group");

    private final Integer type;

    private final String name;


    ChatTypeEnums(Integer type, String name) {
        this.type = type;
        this.name = name;

    }

    public static String getNameByType(Integer type){
        for (ChatTypeEnums item:ChatTypeEnums.values()) {
            if(item.getType().equals(type)){
                return item.getName();
            }
        }
        return null;
    }
}
