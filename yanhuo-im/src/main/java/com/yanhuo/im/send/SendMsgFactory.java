package com.yanhuo.im.send;

import com.yanhuo.im.goeasy.model.Message;

public interface SendMsgFactory{

    void sendMsg(Message message,String data);
}
