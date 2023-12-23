package com.yanhuo.im.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.xo.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhao
 */
@RestController
@RequestMapping("/im")
public class ChatController {

    @Autowired
    ChatService chatService;

    @RequestMapping("sendMsg")
    public Result<?> senMsg(@RequestBody Chat chat){
        boolean flag = chatService.sendMsg(chat);
        return flag?Result.ok(null) : Result.fail("发送消息失败");
    }
}
