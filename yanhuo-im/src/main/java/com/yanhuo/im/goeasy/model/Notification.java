package com.yanhuo.im.goeasy.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class Notification implements Serializable {
    private String title;
    private String body;
}
