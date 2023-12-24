package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.LikeOrCollection;

public interface ChatService {

     /**
      * 发送消息
      * @param chat
      */
     boolean sendMsg(Chat chat);


     /**
      * 点赞收藏评论通知
      */
     boolean noticeUser(LikeOrCollectionDTO likeOrCollectionDTO);

     /**
      * 得到所有的聊天记录(只会保留30天的聊天记录)
      * @param sendUid
      * @param acceptUid
      * @return
      */
     Page<Chat> getChatHistory(long currentPage,long pageSize, String sendUid,String acceptUid);


     /**
      * 删除一条聊天记录
      * @param id
      * @return
      */
     boolean deleteOneChatRecord(String id);

     /**
      * 删除所有聊天记录(缓存删除)
      * @param
      * @return
      */
     boolean deleteAllChatRecord(String sendUid,String acceptUid);
}
