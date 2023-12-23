package com.yanhuo.im.send;

import com.yanhuo.im.goeasy.model.Message;
import com.yanhuo.im.goeasy.model.msgPlayload.ImagePayload;

/**
 * @author xiaozhao
 */
public class SendTextMsg implements SendMsgFactory{

    @Override
    public void sendMsg(Message message) {
        message.setType("textMsg");
        ImagePayload imagePayload = new ImagePayload();
        message.setPayload(imagePayload);
    }
}
