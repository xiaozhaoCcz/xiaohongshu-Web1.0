package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.common.im.Message;
import com.yanhuo.xo.entity.ChatUserRelation;

/**
 * @author xiaozhao
 */
public interface ChatUserRelationService extends IService<ChatUserRelation> {
     /**
      * 发送消息
      *
      * @param message 消息实体
      */
     void sendMessage(Message message);
}
