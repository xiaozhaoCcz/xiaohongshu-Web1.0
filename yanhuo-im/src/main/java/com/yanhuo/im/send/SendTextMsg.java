package com.yanhuo.im.send;

import com.yanhuo.im.goeasy.model.Message;
import com.yanhuo.im.goeasy.model.msgPlayload.ImagePayload;

/**
 * @author xiaozhao
 */
public class SendTextMsg implements SendMsgFactory{

    @Override
    public void sendMsg(Message message,String conetent) {
        message.setType("textMsg");
        ImagePayload imagePayload = new ImagePayload();
        imagePayload.setContent(conetent);

        //用户未读消息+1


        message.setPayload(imagePayload);
    }
}
