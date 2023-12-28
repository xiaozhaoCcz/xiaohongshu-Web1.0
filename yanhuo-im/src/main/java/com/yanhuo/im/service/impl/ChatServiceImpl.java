package com.yanhuo.im.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.im.enums.ChatTypeEnums;
import com.yanhuo.im.goeasy.model.Message;
import com.yanhuo.im.goeasy.model.User;
import com.yanhuo.im.goeasy.result.Result;
import com.yanhuo.im.send.NoticeUserMsg;
import com.yanhuo.im.send.SendImgMsg;
import com.yanhuo.im.send.SendMsgFactory;
import com.yanhuo.im.send.SendTextMsg;
import com.yanhuo.im.service.ChatService;
import com.yanhuo.xo.entity.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * TODO 发送消息的功能
 * @author xiaozhao
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Value("${goeasy.key}")
    private String key;

    @Value("${goeasy.host}")
    private String host;

    @Override
    public boolean sendMsg(Chat chat) {
        String url = "http://"+host+"/v2/im/message";
        Message message = new Message();
        message.setAppkey(key);
        message.setSenderId(chat.getSendUid());
        User toUser = new User();
        toUser.setId(chat.getAcceptUid());
        toUser.setType(ChatTypeEnums.getNameByType(chat.getChatType()));
        message.setTo(toUser);

        SendMsgFactory sendMsgFactory;

        switch (chat.getMsgType()){
            case 1:
                sendMsgFactory = new SendTextMsg();
                break;
            case 2:
                sendMsgFactory = new SendImgMsg();
                break;
            default:
                sendMsgFactory = new NoticeUserMsg();
                break;
        }

        sendMsgFactory.sendMsg(message,chat.getContent());

        String json = JSONUtil.toJsonStr(message);
        String result = HttpRequest.post(url).body(json).execute().body();
        log.info(result);
        Result res = JSONUtil.toBean(result, Result.class);
        if(res.getCode().equals(ResultCodeEnum.SUCCESS.getCode()) ){

            if(chat.getMsgType() == 5){
                 // 用户未读消息+1
            }else{
                // 往聊天记录表中插入树
            }
            // 进行数据库操作
            return true;
        }

        return false;
    }

    @Override
    public Page<Chat> getChatHistory(long currentPage, long pageSize, String sendUid, String acceptUid) {
        return null;
    }

    @Override
    public boolean deleteOneChatRecord(String id) {
        return false;
    }

    @Override
    public boolean deleteAllChatRecord(String sendUid, String acceptUid) {
        return false;
    }

}
