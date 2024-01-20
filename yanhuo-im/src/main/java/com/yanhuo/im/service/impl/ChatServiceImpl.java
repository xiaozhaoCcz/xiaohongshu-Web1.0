package com.yanhuo.im.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.common.im.Message;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.im.factory.MessageFactory;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.im.service.ChatUserRelationService;
import com.yanhuo.im.websocket.WebSocketServer;
import com.yanhuo.xo.dao.ChatDao;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.ChatUserRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ChatServiceImpl extends ServiceImpl<ChatDao, Chat> implements ChatService {

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ChatUserRelationService chatUserRelationService;

    @Autowired
    UserDao userDao;


    @Override
    public void sendMsg(Message message) {
        webSocketServer.sendInfo(message);
        MessageFactory messageFactory = null;

        // 过滤发送的请求类型
        switch (message.getMsgType()) {
            case 0:
                messageFactory = new CountMessageServiceImpl(redisUtils);
                messageFactory.sendMessage(message);
                break;
            case 1:
                chatUserRelationService.sendMessage(message);
                break;
            default:
                break;
        }

    }

    @Override
    public CountMessage getCountMessage() {
        String currentUid = AuthContextHolder.getUserId();
        String messageCountKey = "messageCountKey:" + currentUid;
        if (Boolean.FALSE.equals(redisUtils.hasKey(messageCountKey))) {
            return new CountMessage();
        }
        String json = redisUtils.get(messageCountKey);
        return JSONUtil.toBean(json, CountMessage.class);
    }

    @Override
    public List<ChatUserRelationVo> getChatUserList() {
        String currentUid = AuthContextHolder.getUserId();
        List<ChatUserRelation> chatUserRelationList = chatUserRelationService.list(new QueryWrapper<ChatUserRelation>().eq("accept_uid", currentUid).orderByDesc("create_date"));
        Set<String> uids = chatUserRelationList.stream().map(ChatUserRelation::getSendUid).collect(Collectors.toSet());
        Map<String, User> userMap = userDao.selectBatchIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));
        List<ChatUserRelationVo> result = new ArrayList<>();
        chatUserRelationList.forEach(item -> {
            ChatUserRelationVo chatUserRelationVo = ConvertUtils.sourceToTarget(item, ChatUserRelationVo.class);
            User user = userMap.get(item.getSendUid());
            chatUserRelationVo.setUid(user.getId());
            chatUserRelationVo.setUsername(user.getUsername());
            chatUserRelationVo.setAvatar(user.getAvatar());
            result.add(chatUserRelationVo);
        });
        return result;
    }

    @Override
    public Page<Chat> getAllChatRecord(long currentPage, long pageSize, String acceptUid) {
        String currentUid = AuthContextHolder.getUserId();
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<Chat>()
                .and(e -> e.eq("send_uid", currentUid).eq("accept_uid", acceptUid))
                .or(e->e.eq("send_uid", acceptUid).eq("accept_uid", currentUid))
                .orderByDesc("timestamp");
        return this.page(new Page<>((int) currentPage, (int) pageSize), queryWrapper);
    }
}
