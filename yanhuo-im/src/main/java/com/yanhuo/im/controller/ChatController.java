package com.yanhuo.im.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.im.Message;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.vo.ChatUserRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/im/chat")
@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @RequestMapping("test")
    public void test(){
        System.out.println("test11");
    }


    @RequestMapping("sendMsg")
    public Result<?> sendMsg(@RequestBody Message message) {
        System.out.println("---"+message);
        chatService.sendMsg(message);
        return Result.ok();
    }

    @RequestMapping("getAllChatRecord/{currentPage}/{pageSize}")
    public Result<?> getAllChatRecord(@PathVariable long currentPage, @PathVariable long pageSize,String acceptUid){
        Page<Chat> page =  chatService.getAllChatRecord(currentPage, pageSize,acceptUid);
        return Result.ok(page);
    }

    @RequestMapping("getChatUserList")
    public Result<?> getChatUserList(){
        List<ChatUserRelationVo> list = chatService.getChatUserList();
        return Result.ok(list);
    }

    @RequestMapping("getCountMessage")
    public Result<?> getCountMessage(){
       CountMessage countMessage =  chatService.getCountMessage();
       return Result.ok(countMessage);
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

    @RequestMapping("clearMessageCount")
    public Result<?> clearMessageCount(String sendUid,Integer type){
        chatService.clearMessageCount(sendUid,type);
        return Result.ok();
    }
}
