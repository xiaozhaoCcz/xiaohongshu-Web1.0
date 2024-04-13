package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.im.ChatUtils;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dao.CommentDao;
import com.yanhuo.xo.dto.CommentDTO;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @Autowired
    LikeOrCollectionService likeOrCollectionService;

    @Autowired
    CommentSyncService commentSyncService;

    @Autowired
    ChatUtils chatUtils;

    @Override
    public Page<CommentVo> getOneCommentPageByNoteId(long currentPage, long pageSize, String noteId) {
        return null;
    }

    @Override
    public Object getCommentById(String commentId) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVo saveCommentByDTO(CommentDTO commentDTO) {
        String currentUid = AuthContextHolder.getUserId();
        CommentSync commentsync = ConvertUtils.sourceToTarget(commentDTO, CommentSync.class);
        commentsync.setUid(currentUid);
        commentSyncService.save(commentsync);

        Note note = noteService.getById(commentDTO.getNid());
        note.setCommentCount(note.getCommentCount() + 1);
        noteService.updateById(note);

        CommentVo commentVo = ConvertUtils.sourceToTarget(commentsync, CommentVo.class);
        User user = userService.getById(currentUid);

        commentVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setTime(commentsync.getCreateDate().getTime());

        // 一级评论数量加1
        if (!"0".equals(commentDTO.getPid())) {
            Comment parentComment = this.getById(commentDTO.getPid());
            if (parentComment == null) {
                CommentSync commentSync = commentSyncService.getById(commentDTO.getPid());
                commentSync.setTwoCommentCount(commentSync.getTwoCommentCount() + 1);
                commentSyncService.updateById(commentSync);
            } else {
                parentComment.setTwoCommentCount(parentComment.getTwoCommentCount() + 1);
                this.updateById(parentComment);
            }

        }
        return commentVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncCommentByIds(List<String> commentIds) {
        String currentUid = AuthContextHolder.getUserId();
        if (!commentIds.isEmpty()) {
            List<CommentSync> commentSyncs = commentSyncService.list(new QueryWrapper<CommentSync>().in("id", commentIds));
            List<Comment> comments = ConvertUtils.sourceToTarget(commentSyncs, Comment.class);
            this.saveBatch(comments);

            // TODO 可以使用异步的方式进行通知
            for (Comment comment : comments) {
                if (!comment.getNoteUid().equals(currentUid)) {
                    if (!Objects.equals(comment.getReplyUid(), comment.getNoteUid())) {
                        chatUtils.sendMessage(comment.getNoteUid(), 1);
                    }
                    chatUtils.sendMessage(comment.getReplyUid(), 1);
                }
            }
        }
    }

    @Override
    public IPage<CommentVo> getNoticeComment(long currentPage, long pageSize) {
        Page<CommentVo> result = new Page<>();
        String currentUid = AuthContextHolder.getUserId();

        Page<Comment> commentPage = this.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Comment>().or(e -> e.eq("note_uid", currentUid).or().eq("reply_uid", currentUid)).ne("uid", currentUid).orderByDesc("create_date"));

        List<Comment> commentList = commentPage.getRecords();
        long total = commentPage.getTotal();

        List<CommentVo> commentVoList = new ArrayList<>();
        if (!commentList.isEmpty()) {
            Set<String> uids = commentList.stream().map(Comment::getUid).collect(Collectors.toSet());
            Map<String, User> userMap = userService.listByIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));

            Set<String> nids = commentList.stream().map(Comment::getNid).collect(Collectors.toSet());
            Map<String, Note> noteMap = noteService.listByIds(nids).stream().collect(Collectors.toMap(Note::getId, note -> note));

            // 得到所有回复的评论内容
            Set<String> cids = commentList.stream().filter(item -> !"0".equals(item.getPid())).map(Comment::getReplyId).collect(Collectors.toSet());
            Map<String, Comment> replyCommentMap = new HashMap<>(16);
            if (!cids.isEmpty()) {
                replyCommentMap = this.listByIds(cids).stream().collect(Collectors.toMap(Comment::getId, comment -> comment));
            }


            for (Comment comment : commentList) {
                CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
                User user = userMap.get(comment.getUid());
                Note note = noteMap.get(comment.getNid());
                commentVo.setUsername(user.getUsername())
                        .setAvatar(user.getAvatar())
                        .setTime(comment.getCreateDate().getTime())
                        .setNoteCover(note.getNoteCover());

                if (!"0".equals(comment.getPid())) {
                    Comment replyComment = replyCommentMap.get(comment.getReplyId());
                    commentVo.setReplyContent(replyComment.getContent());
                    if (!comment.getReplyUid().equals(currentUid)) {
                        User replyUser = userMap.get(comment.getReplyUid());
                        commentVo.setReplyUsername(replyUser.getUsername());
                    }
                }
                commentVoList.add(commentVo);
            }
        }
        result.setRecords(commentVoList);
        result.setTotal(total);
        return result;
    }

    @Override
    public Page<CommentVo> getTwoCommentPageByOneCommentId(long currentPage, long pageSize, String oneCommentId) {
        Page<CommentVo> result = new Page<>();
        String currentUid = AuthContextHolder.getUserId();
        Page<Comment> twoCommentPage = this.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Comment>().eq("pid", oneCommentId).orderByDesc("like_count").orderByDesc("create_date"));
        List<Comment> twoCommentList = twoCommentPage.getRecords();
        long total = twoCommentPage.getTotal();

        if (!twoCommentList.isEmpty()) {
            Set<String> uids = twoCommentList.stream().map(Comment::getUid).collect(Collectors.toSet());
            List<User> users = userService.listByIds(uids);
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
            Set<String> replyUids = twoCommentList.stream().map(Comment::getReplyUid).collect(Collectors.toSet());
            Map<String, User> replyUserMap = new HashMap<>(16);
            if (!replyUids.isEmpty()) {
                List<User> replyUsers = userService.listByIds(replyUids);
                replyUserMap = replyUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
            }

            List<CommentVo> commentVos = new ArrayList<>();
            List<LikeOrCollection> likeOrCollections = likeOrCollectionService.list(new QueryWrapper<LikeOrCollection>().eq("uid", currentUid).eq("type", 2));
            List<String> likeComments = likeOrCollections.stream().map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toList());
            for (Comment comment : twoCommentList) {
                CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
                User user = userMap.get(comment.getUid());
                commentVo.setUsername(user.getUsername())
                        .setAvatar(user.getAvatar())
                        .setTime(comment.getCreateDate().getTime())
                        .setIsLike(likeComments.contains(comment.getId()));
                User replyUser = replyUserMap.get(comment.getReplyUid());
                if (replyUser != null) {
                    commentVo.setReplyUsername(replyUser.getUsername());
                }
                commentVos.add(commentVo);
            }
            result.setRecords(commentVos);
        }
        result.setTotal(total);
        return result;
    }

    @Override
    public Page<CommentVo> getCommentPageWithCommentByNoteId(long currentPage, long pageSize, String noteId) {
        //先得到所有的一级评论
        Page<CommentVo> result = new Page<>();
        Page<Comment> oneCommentPage = this.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Comment>().eq("nid", noteId).eq("pid", "0").orderByDesc("like_count"));
        List<Comment> oneCommentList = oneCommentPage.getRecords();
        if (!oneCommentList.isEmpty()) {
            Set<String> oneUids = oneCommentList.stream().map(Comment::getUid).collect(Collectors.toSet());
            long onetotal = oneCommentPage.getTotal();
            String currentUid = AuthContextHolder.getUserId();
            //得到对应的二级评论
            List<String> oneIds = oneCommentList.stream().map(Comment::getId).collect(Collectors.toList());
            List<Comment> twoCommentList = this.list(new QueryWrapper<Comment>().in("pid", oneIds).orderByDesc("like_count").orderByDesc("create_date"));
            Set<String> twoUids = twoCommentList.stream().map(Comment::getUid).collect(Collectors.toSet());
            oneUids.addAll(twoUids);

            List<User> users = userService.listByIds(oneUids);
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            //得到当前用户点赞的评论
            List<LikeOrCollection> likeOrCollections = likeOrCollectionService.list(new QueryWrapper<LikeOrCollection>().eq("uid", currentUid).eq("type", 2));
            List<String> likeComments = likeOrCollections.stream().map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toList());

            Set<String> replyUids = twoCommentList.stream().map(Comment::getReplyUid).collect(Collectors.toSet());
            Map<String, User> replyUserMap = new HashMap<>(16);
            if (!replyUids.isEmpty()) {
                List<User> replyUsers = userService.listByIds(replyUids);
                replyUserMap = replyUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
            }


            List<CommentVo> twoCommentVos = new ArrayList<>();
            for (Comment twoComment : twoCommentList) {
                CommentVo commentVo = ConvertUtils.sourceToTarget(twoComment, CommentVo.class);
                User user = userMap.get(twoComment.getUid());
                commentVo.setUsername(user.getUsername())
                        .setAvatar(user.getAvatar())
                        .setTime(twoComment.getCreateDate().getTime())
                        .setIsLike(likeComments.contains(twoComment.getId()));
                User replyUser = replyUserMap.get(twoComment.getReplyUid());
                if (replyUser != null) {
                    commentVo.setReplyUsername(replyUser.getUsername());
                }
                twoCommentVos.add(commentVo);
            }

            Map<String, List<CommentVo>> twoCommentVoMap = twoCommentVos.stream().collect(Collectors.groupingBy(CommentVo::getPid));

            List<CommentVo> commentVoList = new ArrayList<>();

            for (Comment oneComment : oneCommentList) {
                CommentVo commentVo = ConvertUtils.sourceToTarget(oneComment, CommentVo.class);
                User user = userMap.get(oneComment.getUid());
                commentVo.setUsername(user.getUsername())
                        .setAvatar(user.getAvatar())
                        .setTime(oneComment.getCreateDate().getTime())
                        .setIsLike(likeComments.contains(oneComment.getId()));
                List<CommentVo> children = twoCommentVoMap.get(oneComment.getId());

                if (children != null && children.size() > 3) {
                    children = children.subList(0, 3);
                }
                commentVo.setChildren(children);
                commentVoList.add(commentVo);
            }

            result.setRecords(commentVoList);
            result.setTotal(onetotal);
        }
        return result;
    }

    @Override
    public Map<String, Object> scrollComment(String commentId) {
        Map<String, Object> resMap = new HashMap<>(16);
        Comment comment = this.getById(commentId);
        String pid = comment.getPid();
        int page1 = 1;
        int page2 = 1;
        int limit1 = 7;
        int limit2 = 10;
        long total = 0;
        boolean flag = false;
        List<CommentVo> comments = new ArrayList<>();
        if ("0".equals(pid)) {
            while (!flag) {
                Page<CommentVo> allOneCommentPage = this.getCommentPageWithCommentByNoteId(page1, limit1, comment.getNid());
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<String> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(commentId)) {
                    flag = true;
                    total = allOneCommentPage.getTotal();
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        } else {
            boolean flag2 = false;

            while (!flag) {
                IPage<CommentVo> allOneCommentPage = this.getCommentPageWithCommentByNoteId(page1, limit1, comment.getNid());
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<String> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(pid)) {
                    for (CommentVo commentVo : commentVoList) {
                        if (Objects.equals(commentVo.getId(), pid)) {
                            List<CommentVo> comments2 = new ArrayList<>();
                            flag = true;
                            total = allOneCommentPage.getTotal();
                            while (!flag2) {
                                IPage<CommentVo> allTwoCommentPage = this.getTwoCommentPageByOneCommentId(page2, limit2, pid);
                                List<CommentVo> commentVoList2 = allTwoCommentPage.getRecords();
                                List<String> ids = commentVoList2.stream().map(CommentVo::getId).collect(Collectors.toList());
                                if (ids.contains(commentId)) {
                                    flag2 = true;
                                } else {
                                    page2++;
                                }
                                comments2.addAll(commentVoList2);
                            }
                            commentVo.setChildren(comments2);
                        }
                    }
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        }
        resMap.put("records", comments);
        resMap.put("total", total);
        resMap.put("page1", page1);
        resMap.put("page2", page2);
        return resMap;
    }

    @Override
    public void deleteCommentById(String commentId) {

    }
}
