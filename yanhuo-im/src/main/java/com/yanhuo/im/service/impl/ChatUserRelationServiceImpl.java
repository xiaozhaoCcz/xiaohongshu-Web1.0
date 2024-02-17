package com.yanhuo.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.im.Message;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.im.service.ChatUserRelationService;
import com.yanhuo.im.websocket.WebSocketServer;
import com.yanhuo.xo.dao.ChatUserRelationDao;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.ChatUserRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Service
public class ChatUserRelationServiceImpl extends ServiceImpl<ChatUserRelationDao, ChatUserRelation> implements ChatUserRelationService {

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    ChatService chatService;

    @Autowired
    UserDao userDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(Message message) {
        String content = String.valueOf(message.getContent());
        // 当前用户插入
        saveMessage(message,0);
        saveMessage(message,1);

        getUserChatList(message,0);
        getUserChatList(message,1);

        Chat chat = ConvertUtils.sourceToTarget(message, Chat.class);
        chat.setTimestamp(System.currentTimeMillis());
        chat.setContent(content);
        chatService.save(chat);
    }

    private void saveMessage(Message message,Integer type) {
        String content = String.valueOf(message.getContent());
        ChatUserRelation chatUserRelation =null;
        if(type==0){
            chatUserRelation = this.getOne(new QueryWrapper<ChatUserRelation>().eq("send_uid", message.getSendUid()).eq("accept_uid", message.getAcceptUid()));
        }else{
            chatUserRelation =  this.getOne(new QueryWrapper<ChatUserRelation>().eq("send_uid", message.getAcceptUid()).eq("accept_uid", message.getSendUid()));
        }

        if(chatUserRelation!=null){
            chatUserRelation.setContent(content);
            chatUserRelation.setTimestamp(System.currentTimeMillis());
            if(type==0) {
                chatUserRelation.setCount(chatUserRelation.getCount() + 1);
            }else{
                chatUserRelation.setCount(0);
            }
            this.updateById(chatUserRelation);
        }else{
            chatUserRelation = new ChatUserRelation();
            if(type==0){
                chatUserRelation.setSendUid(message.getSendUid());
                chatUserRelation.setAcceptUid(message.getAcceptUid());
                chatUserRelation.setCount(1);
            }else{
                chatUserRelation.setSendUid(message.getAcceptUid());
                chatUserRelation.setAcceptUid(message.getSendUid());
                chatUserRelation.setCount(0);
            }
            chatUserRelation.setContent(content);
            chatUserRelation.setTimestamp(System.currentTimeMillis());
            this.save(chatUserRelation);
        }
    }

    public void getUserChatList(Message message,Integer type){
        List<ChatUserRelation> chatUserRelationList;
        if(type==0) {
            chatUserRelationList = this.list(new QueryWrapper<ChatUserRelation>().eq("accept_uid", message.getSendUid()).orderByDesc("create_date"));
        }else{
            chatUserRelationList = this.list(new QueryWrapper<ChatUserRelation>().eq("accept_uid", message.getAcceptUid()).orderByDesc("create_date"));
        }

        Set<String> uids = chatUserRelationList.stream().map(ChatUserRelation::getSendUid).collect(Collectors.toSet());
        Map<String, User> userMap = userDao.selectBatchIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));

        List<ChatUserRelationVo> chatUserRelationVoList = new ArrayList<>();
        chatUserRelationList.forEach(item->{
            ChatUserRelationVo chatUserRelationVo = ConvertUtils.sourceToTarget(item, ChatUserRelationVo.class);
            User user = userMap.get(item.getSendUid());
            chatUserRelationVo.setUid(user.getId());
            chatUserRelationVo.setUsername(user.getUsername());
            chatUserRelationVo.setAvatar(user.getAvatar());
            chatUserRelationVoList.add(chatUserRelationVo);
        });

        Message currentUserMessage = new Message();
        if(type==0){
            currentUserMessage.setAcceptUid(message.getSendUid());
        }else{
            currentUserMessage.setAcceptUid(message.getAcceptUid());
        }
        currentUserMessage.setContent(chatUserRelationVoList);
        currentUserMessage.setMsgType(5);
        webSocketServer.sendInfo(currentUserMessage);
    }
}
