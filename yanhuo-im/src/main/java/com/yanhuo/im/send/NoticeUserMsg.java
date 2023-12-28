package com.yanhuo.im.send;

import cn.hutool.json.JSONUtil;
import com.yanhuo.im.goeasy.model.Message;
import com.yanhuo.im.goeasy.model.UserNoticeCount;
import com.yanhuo.im.goeasy.model.User;
import com.yanhuo.im.goeasy.model.msgPlayload.ImagePayload;

public class NoticeUserMsg  implements SendMsgFactory{

    @Override
    public void sendMsg(Message message, String type) {

        //得到通知的用户
        User to = message.getTo();
        UserNoticeCount userNoticeCount = new UserNoticeCount();
        userNoticeCount.setUid(to.getId());
        switch (type){
            case "likeOrCollect":
                userNoticeCount.setNotifyLikeOrCollectionCount(userNoticeCount.getNotifyLikeOrCollectionCount()+1);
                break;
            case "comment":
                userNoticeCount.setNotifyCommentCount(userNoticeCount.getNotifyCommentCount()+1);
                break;
            case "follow":
                userNoticeCount.setNotifyFanCount(userNoticeCount.getNotifyFanCount()+1);
                break;
            default:
                break;
        }
        //重新设置
        ImagePayload imagePayload = new ImagePayload();
        imagePayload.setContent(JSONUtil.toJsonStr(imagePayload));
       message.setPayload(imagePayload);
    }

}
