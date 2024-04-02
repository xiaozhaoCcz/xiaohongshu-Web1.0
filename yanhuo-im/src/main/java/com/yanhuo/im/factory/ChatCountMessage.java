package com.yanhuo.im.factory;

import cn.hutool.json.JSONUtil;
import com.yanhuo.common.constant.ImConstant;
import com.yanhuo.common.im.Message;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.im.entity.CountMessage;

/**
 * @author xiaozhao
 */
public class ChatCountMessage implements MessageFactory{

    RedisUtils redisUtils;

    public ChatCountMessage(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }
    @Override
    public void sendMessage(Message message) {
        String messageCountKey = ImConstant.MESSAGE_COUNT_KEY + message.getAcceptUid();
        CountMessage countMessage = JSONUtil.toBean(JSONUtil.toJsonStr(message.getContent()), CountMessage.class);
        countMessage.setUid(message.getAcceptUid());
        redisUtils.set(messageCountKey, JSONUtil.toJsonStr(countMessage));
    }
}
