package com.yanhuo.platform;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.service.AlbumNoteRelationService;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.entity.Album;
import com.yanhuo.xo.entity.AlbumNoteRelation;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = PlatformApplication.class)
public class PlatformApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumNoteRelationService albumNoteRelationService;

    @Test
    public void test1(){
        List<User> userList = userService.list();
        System.out.println(userList);

        Album album = albumService.getById("1661900149283938306");

        for (User user: userList) {
            Album album1 = ConvertUtils.sourceToTarget(album,Album.class);
            album1.setId(null);
            album1.setTitle("默认专辑");
            album1.setUid(user.getId());
            albumService.save(album1);
        }
    }

    @Test
    public void test2(){
        List<Note> list = noteService.list();
        for (Note note: list) {
            Album album = albumService.getOne(new QueryWrapper<Album>().eq("uid",note.getUid()));
            AlbumNoteRelation albumNoteRelation = new AlbumNoteRelation();
           albumNoteRelation.setAid(album.getId());
           albumNoteRelation.setNid(note.getId());
           albumNoteRelationService.save(albumNoteRelation);
        }
    }

    @Test
    public void test3(){
        List<Note> list = noteService.list();
        for (Note note: list) {
            note.setTitle(note.getContent());
            noteService.updateById(note);
        }
    }

    @Test
    public void test4(){
        List<Note> list = noteService.list(new QueryWrapper<Note>().like("note_cover", "https://"));
        List<String> collect = list.stream().map(Note::getId).collect(Collectors.toList());
        noteService.removeBatchByIds(collect);

    }
}
