package com.yanhuo.im.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.im.Message;
import com.yanhuo.common.validator.myVaildator.noLogin.NoLoginIntercept;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.vo.ChatUserRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaozhao
 */
@RequestMapping("/chat")
@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 发送消息
     *
     * @param message 消息实体
     * @return success
     */
    @PostMapping("sendMsg")
    @NoLoginIntercept
    public Result<?> sendMsg(@RequestBody Message message) {
        chatService.sendMsg(message);
        return Result.ok();
    }

    /**
     * 获取所有的聊天记录
     *
     * @param currentPage 分页
     * @param pageSize    分页数
     * @param acceptUid   接收方的用户id
     * @return 聊天记录
     */
    @GetMapping("getAllChatRecord/{currentPage}/{pageSize}")
    public Result<?> getAllChatRecord(@PathVariable long currentPage, @PathVariable long pageSize, String acceptUid) {
        Page<Chat> page = chatService.getAllChatRecord(currentPage, pageSize, acceptUid);
        return Result.ok(page);
    }

    /**
     * 获取当前用户下所有聊天的用户信息
     *
     * @return 聊天的用户信息
     */
    @GetMapping("getChatUserList")
    public Result<?> getChatUserList() {
        List<ChatUserRelationVo> list = chatService.getChatUserList();
        return Result.ok(list);
    }

    /**
     * 得到所有聊天的记录数量
     *
     * @return 聊天数量
     */
    @GetMapping("getCountMessage")
    public Result<?> getCountMessage() {
        CountMessage countMessage = chatService.getCountMessage();
        return Result.ok(countMessage);
    }

    @GetMapping("deleteMsg")
    public String deleteMsg() {
        return null;
    }

    @GetMapping("deleteAllChatRecord")
    public String deleteAllChatRecord() {
        return null;
    }

    @GetMapping("deleteChatUser")
    public String deleteChatUser() {
        return null;
    }

    /**
     * 清除聊天数量
     *
     * @param sendUid 发送方的用户id
     * @param type    类型
     * @return success
     */
    @GetMapping("clearMessageCount")
    public Result<?> clearMessageCount(String sendUid, Integer type) {
        chatService.clearMessageCount(sendUid, type);
        return Result.ok();
    }

    @RequestMapping("closeChat/{sendUid}")
    public boolean closeChat(@PathVariable("sendUid") String sendUid){
        return chatService.closeChat(sendUid);
    }
}
