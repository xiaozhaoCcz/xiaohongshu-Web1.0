package com.yanhuo.im.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.im.entity.Message;
import com.yanhuo.im.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat")
@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @RequestMapping("/sendMsg")
    public Result<?> sendMsg(@RequestBody Message<?> message) {
        chatService.sendMsg(message);
        return Result.ok();
    }

    @RequestMapping("getAllChatRecord/{currentPage}/{pageSize}")
    public String getAllChatRecord(){
        return null;
    }

    @RequestMapping("getChatUserList/{currentPage}/{pageSize}")
    public String getChatUserList(){
        return null;
    }

    @RequestMapping("deleteMsg")
    public String deleteMsg(){
        return null;
    }

    @RequestMapping("deleteAllChatRecord")
    public String deleteAllChatRecord(){
        return null;
    }

    @RequestMapping("deleteChatUser")
    public String deleteChatUser(){
        return null;
    }

    @RequestMapping("clearNoViewCount")
    public String clearNoViewCount(){
        return null;
    }
}
