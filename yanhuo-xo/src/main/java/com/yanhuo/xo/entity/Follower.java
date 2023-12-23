package com.yanhuo.xo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("t_follower")
public class Follower extends BaseEntity {

    private String uid;

    private String fid;

}
