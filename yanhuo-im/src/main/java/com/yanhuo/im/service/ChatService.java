package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.im.entity.Message;
import com.yanhuo.xo.entity.Chat;

public interface ChatService extends IService<Chat> {
    void sendMsg(Message<?> message);
}
