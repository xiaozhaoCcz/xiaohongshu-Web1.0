package com.yanhuo.im.factory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhuo.common.im.Message;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.im.websocket.WebSocketServer;
import com.yanhuo.xo.dao.ChatUserRelationDao;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.ChatUserRelationVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author xiaozhao
 */
public class ChatUserMessage implements MessageFactory {

    private final WebSocketServer webSocketServer;

    private final ChatService chatService;

    private final UserDao userDao;

    private final ChatUserRelationDao chatUserRelationDao;

    public ChatUserMessage(WebSocketServer webSocketServer, ChatService chatService, UserDao userDao, ChatUserRelationDao chatUserRelationDao) {
        this.webSocketServer = webSocketServer;
        this.chatService = chatService;
        this.userDao = userDao;
        this.chatUserRelationDao = chatUserRelationDao;
    }

    @Override
    public void sendMessage(Message message) {
        String content = String.valueOf(message.getContent());
        // 当前用户插入
        saveMessage(message, 0);
        saveMessage(message, 1);

        getUserChatList(message, 0);
        getUserChatList(message, 1);

        Chat chat = ConvertUtils.sourceToTarget(message, Chat.class);
        chat.setTimestamp(System.currentTimeMillis());
        chat.setContent(content);
        chatService.save(chat);
    }

    private void saveMessage(Message message, Integer type) {
        String content = String.valueOf(message.getContent());
        String sendUid = type == 0 ? message.getSendUid() : message.getAcceptUid();
        String acceptUid = type == 0 ? message.getAcceptUid() : message.getSendUid();
        ChatUserRelation chatUserRelation = chatUserRelationDao.selectOne(new QueryWrapper<ChatUserRelation>().eq("send_uid", sendUid).eq("accept_uid", acceptUid));

        if (chatUserRelation != null) {
            chatUserRelation.setContent(content);
            chatUserRelation.setTimestamp(System.currentTimeMillis());
            chatUserRelation.setCount(type == 0 ? chatUserRelation.getCount() + 1 : 0);
            chatUserRelationDao.updateById(chatUserRelation);
        } else {
            chatUserRelation = new ChatUserRelation();
            chatUserRelation.setSendUid(sendUid);
            chatUserRelation.setAcceptUid(acceptUid);
            chatUserRelation.setCount(type == 0 ? 1 : 0);
            chatUserRelation.setContent(content);
            chatUserRelation.setTimestamp(System.currentTimeMillis());
            chatUserRelationDao.insert(chatUserRelation);
        }
    }

    public void getUserChatList(Message message, Integer type) {
        String acceptUid = type == 0 ? message.getSendUid() : message.getAcceptUid();
        List<ChatUserRelation> chatUserRelationList = chatUserRelationDao.selectList(new QueryWrapper<ChatUserRelation>().eq("accept_uid", acceptUid).orderByDesc("create_date"));
        Set<String> uids = chatUserRelationList.stream().map(ChatUserRelation::getSendUid).collect(Collectors.toSet());
        Map<String, User> userMap = userDao.selectBatchIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));
        List<ChatUserRelationVo> chatUserRelationVoList = new ArrayList<>();
        chatUserRelationList.forEach(item -> {
            ChatUserRelationVo chatUserRelationVo = ConvertUtils.sourceToTarget(item, ChatUserRelationVo.class);
            User user = userMap.get(item.getSendUid());
            chatUserRelationVo.setUid(user.getId());
            chatUserRelationVo.setUsername(user.getUsername());
            chatUserRelationVo.setAvatar(user.getAvatar());
            chatUserRelationVoList.add(chatUserRelationVo);
        });

        Message currentUserMessage = new Message();
        currentUserMessage.setAcceptUid(acceptUid);
        currentUserMessage.setContent(chatUserRelationVoList);
        currentUserMessage.setMsgType(5);
        webSocketServer.sendInfo(currentUserMessage);
    }
}
