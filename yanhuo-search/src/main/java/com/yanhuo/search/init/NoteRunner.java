package com.yanhuo.search.init;

import cn.hutool.core.date.DateUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.search.common.NoteConstant;
import com.yanhuo.search.config.ESConfig;
import com.yanhuo.search.service.NoteService;
import com.yanhuo.search.service.UserService;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.NoteSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
//@Component  //被 spring 容器管理
//@Order(1)   //如果多个自定义的 ApplicationRunner  ，用来标明执行的顺序
@Slf4j
public class NoteRunner implements ApplicationRunner {

    @Autowired
    NoteService noteService;

    @Autowired
    ESConfig esConfig;


    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addNoteBulkData();
    }


    private void addNoteBulkData() throws IOException {
        List<Note> noteList = noteService.list();

        List<BulkOperation> result = new ArrayList<>();

        List<String> uids = noteList.stream().map(Note::getUid).collect(Collectors.toList());
        List<User> userList = userService.listByIds(uids);
        HashMap<String, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });
        User user;
        for (Note note : noteList) {
            NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
            user = userMap.get(note.getUid());
            noteSearchVo.setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(note.getUpdateDate().getTime());
            result.add(new BulkOperation.Builder().create(
                    d -> d.document(noteSearchVo).id(noteSearchVo.getId()).index(NoteConstant.NOTE_INDEX)).build());
        }
        BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index(NoteConstant.NOTE_INDEX).operations(result));
        log.info("bulkResponse.items{}",bulkResponse.items());
        esConfig.close();
    }
}
