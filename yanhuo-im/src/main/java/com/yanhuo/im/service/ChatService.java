package com.yanhuo.im.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.common.im.Message;
import com.yanhuo.im.entity.CountMessage;
import com.yanhuo.xo.entity.Chat;
import com.yanhuo.xo.entity.ChatUserRelation;
import com.yanhuo.xo.vo.ChatUserRelationVo;

import java.util.List;

/**
 * @author xiaozhao
 */
public interface ChatService extends IService<Chat> {
    /**
     * 发送消息
     *
     * @param message 消息实体
     */
    void sendMsg(Message message);

    /**
     * 得到所有聊天的记录数量
     *
     * @return 聊天数量
     */
    CountMessage getCountMessage();

    /**
     * 获取当前用户下所有聊天的用户信息
     *
     * @return 聊天的用户信息
     */
    List<ChatUserRelationVo> getChatUserList();

    /**
     * 获取所有的聊天记录
     *
     * @param currentPage 分页
     * @param pageSize    分页数
     * @param acceptUid   接收方的用户id
     * @return 聊天记录
     */
    Page<Chat> getAllChatRecord(long currentPage, long pageSize, String acceptUid);

    /**
     * 清除聊天数量
     *
     * @param sendUid 发送方的用户id
     * @param type    类型
     * @return success
     */
    void clearMessageCount(String sendUid, Integer type);

    boolean closeChat(String sendUid);
}
