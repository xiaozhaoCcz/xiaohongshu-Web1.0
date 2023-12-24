package com.yanhuo.im.send;

import com.yanhuo.im.goeasy.model.Message;

public interface SendMsgFactory<T>{

    void sendMsg(Message message,T data);
}
