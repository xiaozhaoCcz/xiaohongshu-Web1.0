package com.yanhuo.im.service.impl;

import cn.hutool.json.JSONUtil;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.common.im.Message;
import com.yanhuo.im.factory.MessageFactory;


public class CountMessageServiceImpl implements MessageFactory {

    RedisUtils redisUtils;
    CountMessageServiceImpl(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }

    @Override
    public void sendMessage(Message message) {
        String messageCountKey = "messageCountKey:" + message.getAcceptUid();
        CountMessage countMessage = JSONUtil.toBean(JSONUtil.toJsonStr(message.getContent()),CountMessage.class);
        countMessage.setUid(message.getAcceptUid());
        redisUtils.set(messageCountKey,JSONUtil.toJsonStr(countMessage));
    }
}
