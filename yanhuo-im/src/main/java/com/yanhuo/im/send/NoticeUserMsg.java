package com.yanhuo.im.send;

import com.yanhuo.im.goeasy.model.Message;
import com.yanhuo.im.goeasy.model.UserNoticeCount;
import com.yanhuo.im.goeasy.model.User;

public class NoticeUserMsg  implements SendMsgFactory<Integer>{

    @Override
    public void sendMsg(Message message, Integer type) {

        //得到通知的用户
        User to = message.getTo();
        String id = to.getId();

        UserNoticeCount userNoticeCount = new UserNoticeCount();

        switch (type){
            case 1:
                userNoticeCount.setNotifyLikeOrCollectionCount(userNoticeCount.getNotifyLikeOrCollectionCount()+1);
                break;
            default:
                break;
        }

        //重新设置

       message.setPayload();

    }

}
