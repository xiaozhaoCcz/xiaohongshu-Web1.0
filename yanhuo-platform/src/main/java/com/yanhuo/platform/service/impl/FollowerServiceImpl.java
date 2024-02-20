package com.yanhuo.platform.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.platform.im.ChatUtils;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.dao.FollowerDao;
import com.yanhuo.xo.entity.Follower;
import com.yanhuo.xo.entity.LikeOrCollection;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerDao, Follower> implements FollowerService {

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @Autowired
    LikeOrCollectionService likeOrCollectionService;

    @Autowired
    ChatUtils chatUtils;

    @Override
    public Page<TrendVo> getFollowTrendPage(long currentPage, long pageSize) {
        Page<TrendVo> page = new Page<>();
        // 得到当前用户所有关注的用户
        String currentUid = AuthContextHolder.getUserId();
        List<Follower> followers = this.list(new QueryWrapper<Follower>().eq("uid", currentUid));
        List<String> fids = followers.stream().map(Follower::getFid).collect(Collectors.toList());
        fids.add(currentUid);
        Page<Note> notePage = noteService.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Note>().in("uid", fids).orderByDesc("update_date"));
        List<Note> notes = notePage.getRecords();
        List<TrendVo> trendVos = new ArrayList<>();
        if (!notes.isEmpty()) {
            //得到所有用户的图片
            List<String> ids = notes.stream().map(Note::getUid).collect(Collectors.toList());
            List<User> users = userService.listByIds(ids);
            HashMap<String, User> userMap = new HashMap<>();
            users.forEach(item -> userMap.put(item.getId(), item));
            // 是否点赞
            List<LikeOrCollection> likeOrCollections = likeOrCollectionService.list(new QueryWrapper<LikeOrCollection>().eq("uid", currentUid).eq("type", 1));
            List<String> likeOrCollectionIds = likeOrCollections.stream().map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toList());

            for (Note note : notes) {
                TrendVo trendVo = new TrendVo();
                User user = userMap.get(note.getUid());
                trendVo.setUid(user.getId())
                        .setUsername(user.getUsername())
                        .setAvatar(user.getAvatar())
                        .setNid(note.getId())
                        .setTime(note.getUpdateDate().getTime())
                        .setContent(note.getContent())
                        .setCommentCount(note.getCommentCount())
                        .setLikeCount(note.getLikeCount())
                        .setIsLike(likeOrCollectionIds.contains(note.getId()))
                        .setIsLoading(false);
                String urls = note.getUrls();
                List<String> imgList = JSONUtil.toList(urls, String.class);
                if (imgList.size() > 4) {
                    List<String> subList = imgList.subList(0, 4);
                    trendVo.setImgUrls(subList);
                } else {
                    trendVo.setImgUrls(imgList);
                }
                trendVos.add(trendVo);
            }
        }
        long total = notePage.getTotal();
        page.setTotal(total);
        page.setRecords(trendVos);
        return page;
    }

    @Override
    public Page<FollowerVo> getFriendPage(long currentPage, long pageSize, Integer type) {
        return null;
    }

    // TODO 需要优化
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void followById(String followerId) {
        Follower follower = new Follower();
        String userId = AuthContextHolder.getUserId();
        follower.setUid(userId);
        follower.setFid(followerId);
        // 得到当前用户
        User currentUser = userService.getById(userId);
        User followerUser = userService.getById(followerId);
        if (isFollow(followerId)) {
            currentUser.setFollowerCount(currentUser.getFollowerCount() - 1);
            followerUser.setFanCount(followerUser.getFanCount() - 1);
            this.remove(new QueryWrapper<Follower>().eq("uid", userId).eq("fid", followerId));
        } else {
            currentUser.setFollowerCount(currentUser.getFollowerCount() + 1);
            followerUser.setFanCount(followerUser.getFanCount() + 1);
            this.save(follower);
            chatUtils.sendMessage(followerId, 2);
        }
        userService.updateById(currentUser);
        userService.updateById(followerUser);
    }

    @Override
    public boolean isFollow(String followerId) {
        String userId = AuthContextHolder.getUserId();
        long count = this.count(new QueryWrapper<Follower>().eq("uid", userId).eq("fid", followerId));
        return count > 0;
    }

    @Override
    public Page<FollowerVo> getNoticeFollower(long currentPage, long pageSize) {
        Page<FollowerVo> result = new Page<>();
        String userId = AuthContextHolder.getUserId();

        Page<Follower> followerPage = this.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Follower>().eq("fid", userId).ne("uid", userId).orderByDesc("create_date"));
        List<Follower> followerList = followerPage.getRecords();
        long total = followerPage.getTotal();

        Set<String> uids = followerList.stream().map(Follower::getUid).collect(Collectors.toSet());
        Map<String, User> userMap = userService.listByIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));

        // 得到当前用户的所有关注
        List<Follower> followers = this.list(new QueryWrapper<Follower>().eq("uid", userId));
        Set<String> followerSet = followers.stream().map(Follower::getFid).collect(Collectors.toSet());

        List<FollowerVo> followerVoList = new ArrayList<>();
        followerList.forEach(item -> {
            FollowerVo followerVo = new FollowerVo();
            User user = userMap.get(item.getUid());
            followerVo.setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(item.getCreateDate().getTime())
                    .setIsFollow(followerSet.contains(item.getUid()));
            followerVoList.add(followerVo);
        });
        result.setRecords(followerVoList);
        result.setTotal(total);
        return result;
    }
}
