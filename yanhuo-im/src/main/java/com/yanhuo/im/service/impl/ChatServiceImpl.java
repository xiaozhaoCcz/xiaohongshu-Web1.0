package com.yanhuo.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.im.entity.Message;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.im.websocket.WebSocketServer;
import com.yanhuo.xo.dao.ChatDao;
import com.yanhuo.xo.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatDao, Chat> implements ChatService {

    @Autowired
    WebSocketServer webSocketServer;

    @Override
    public void sendMsg(Message<?> message) {
        webSocketServer.sendInfo(message);
    }
}
