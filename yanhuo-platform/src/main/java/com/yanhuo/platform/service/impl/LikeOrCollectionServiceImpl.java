package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.xo.dao.LikeOrCollectionDao;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.Comment;
import com.yanhuo.xo.entity.LikeOrCollection;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhao
 */
@Service
public class LikeOrCollectionServiceImpl extends ServiceImpl<LikeOrCollectionDao, LikeOrCollection> implements LikeOrCollectionService {

    @Autowired
    NoteService noteService;

    @Autowired
    CommentService commentService;

    @Override
    public void likeByDTO(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();

        // 点赞
        if(isLikeOrCollection(likeOrCollectionDTO)){
            this.remove(new QueryWrapper<LikeOrCollection>().eq("uid",currentUid).eq("like_or_collection_id",likeOrCollectionDTO.getLikeOrCollectionId()).eq("type",likeOrCollectionDTO.getType()));
            updateLikeCount(likeOrCollectionDTO,-1);
        }else{
            // 点赞评论或者笔记
            LikeOrCollection likeOrCollection = ConvertUtils.sourceToTarget(likeOrCollectionDTO, LikeOrCollection.class);
            likeOrCollection.setTimestamp(System.currentTimeMillis());
            likeOrCollection.setUid(currentUid);
            this.save(likeOrCollection);
            updateLikeCount(likeOrCollectionDTO,1);
            // TODO 发送消息
        }
    }

    private void updateLikeCount(LikeOrCollectionDTO likeOrCollectionDTO,int val) {
        if(likeOrCollectionDTO.getType() == 1){
            Note note = noteService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
            note.setLikeCount(note.getLikeCount()+val);
            noteService.updateById(note);
        }else{
            Comment comment = commentService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
            comment.setLikeCount(comment.getLikeCount()+val);
            commentService.updateById(comment);
        }
    }

    @Override
    public boolean isLikeOrCollection(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();
        long count = this.count(new QueryWrapper<LikeOrCollection>().eq("uid",currentUid).eq("like_or_collection_id",likeOrCollectionDTO.getLikeOrCollectionId()).eq("type",likeOrCollectionDTO.getType()));
        return count>0;
    }
}
