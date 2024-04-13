package com.yanhuo.platform.xxljob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.NoteSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Component
@Slf4j
public class NoteJobHandler {

    @Autowired
    EsClient esClient;

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @Autowired
    TagNoteRelationService tagNoteRelationService;

    @Autowired
    TagService tagService;

    @Autowired
    CategoryService categoryService;


    @XxlJob("noteJobHandler")
    public void execute() throws Exception {
        log.info("es数据同步开始--------------------------------");
        List<NoteSearchVo> noteSearchVoList = new ArrayList<>();
        List<Note> noteList = noteService.list();
        List<String> uids = noteList.stream().map(Note::getUid).collect(Collectors.toList());
        List<User> userList = userService.listByIds(uids);
        HashMap<String, User> userMap = new HashMap<>();
        userList.forEach(item -> userMap.put(item.getId(), item));
        User user;
        for (Note note : noteList) {
            NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
            user = userMap.get(note.getUid());
            //懒得优化了
            List<TagNoteRelation> tagNoteRelationList = tagNoteRelationService.list(new QueryWrapper<TagNoteRelation>().eq("nid", note.getId()));
            if (!tagNoteRelationList.isEmpty()) {
                Set<String> tids = tagNoteRelationList.stream().map(TagNoteRelation::getTid).collect(Collectors.toSet());
                List<Tag> tags = tagService.listByIds(tids);
                String tagStr = tags.stream().map(Tag::getTitle).collect(Collectors.joining(","));
                noteSearchVo.setTags(tagStr);
            } else {
                noteSearchVo.setTags("");
            }

            Category category = categoryService.getById(note.getCid());
            Category parentCategory = categoryService.getById(note.getCpid());
            noteSearchVo.setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setCategoryName(category.getTitle())
                    .setCategoryParentName(parentCategory.getTitle())
                    .setTime(note.getUpdateDate().getTime())
                    .setIsLoading(false);
            noteSearchVoList.add(noteSearchVo);
        }
        esClient.addNoteBulkData(noteSearchVoList);
        log.info("es数据同步结束--------------------------------");
    }
}
