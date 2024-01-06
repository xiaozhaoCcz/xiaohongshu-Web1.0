package com.yanhuo.platform.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.dao.FollowerDao;
import com.yanhuo.xo.entity.Follower;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.AuthContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerDao, Follower> implements FollowerService {

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @Override
    public Page<TrendVo> getFollowTrendPage(long currentPage, long pageSize) {
        Page<TrendVo> page = new Page<>();
        // 得到当前用户所有关注的用户
        String currentUid = AuthContextHolder.getUserId();
        List<Follower> followers = this.list(new QueryWrapper<Follower>().eq("uid", currentUid));
        List<String> fids = followers.stream().map(Follower::getFid).collect(Collectors.toList());
        fids.add(currentUid);
        Page<Note> notePage =  noteService.page(new Page<>((int)currentPage,(int)pageSize),new QueryWrapper<Note>().in("uid", fids).orderByDesc("update_date"));
        List<Note> notes = notePage.getRecords();
        List<TrendVo> trendVos = new ArrayList<>();
        if (!notes.isEmpty()){
            //得到所有用户的图片
            List<String> ids = notes.stream().map(Note::getUid).collect(Collectors.toList());
            List<User> users = userService.listByIds(ids);
            HashMap<String,User> userMap = new HashMap<>();
            users.forEach(item-> userMap.put(item.getId(),item));
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
                        .setLikeCount(note.getLikeCount());

                String urls = note.getUrls();
                List<String> imgList = JSONUtil.toList(urls, String.class);
                if(imgList.size()>4){
                    List<String> subList = imgList.subList(0, 4);
                    trendVo.setImgUrls(subList);
                }else {
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

    @Override
    public void followById(String followerId) {

    }

    @Override
    public boolean isFollow(String followerId) {
        return false;
    }

    @Override
    public void deleteFollowerById(String followerId) {

    }
}
