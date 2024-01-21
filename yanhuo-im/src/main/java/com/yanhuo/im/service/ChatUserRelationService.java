package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.common.im.Message;
import com.yanhuo.xo.entity.ChatUserRelation;

public interface ChatUserRelationService extends IService<ChatUserRelation>{

     void  sendMessage(Message message);
}
