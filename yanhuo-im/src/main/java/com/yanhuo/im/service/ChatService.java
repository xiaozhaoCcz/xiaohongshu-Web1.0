package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.common.im.Message;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.vo.ChatUserRelationVo;

import java.util.List;

public interface ChatService extends IService<Chat> {
    void sendMsg(Message message);

    CountMessage getCountMessage();

    List<ChatUserRelationVo> getChatUserList();

    Page<Chat> getAllChatRecord(long currentPage, long pageSize, String acceptUid);

    void clearMessageCount(String sendUid);
}
