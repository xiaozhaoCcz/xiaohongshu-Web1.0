package com.yanhuo.platform;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.client.OssClient;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.NoteSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = PlatformApplication.class)
@Slf4j
public class PlatformApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumNoteRelationService albumNoteRelationService;

    @Autowired
    EsClient esClient;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OssClient ossClient;

    @Autowired
    TagNoteRelationService tagNoteRelationService;

    @Autowired
    TagService tagService;


    // 直接允许此方法同步es数据
    @Test
    public void syncEsData(){
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
    }

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


    String basePathUrl = "C:\\Users\\48423\\Desktop\\imgs\\";
    String[] paths = new String[]{"Anime\\", "head\\", "movies\\"};
    @Test
    public void test5() {
        String baseUrl = basePathUrl + paths[0];
        List<String> list = getF(baseUrl);
        list.forEach(item -> {
            this.saveNote(baseUrl, item, "1655460777328205825", "动漫", "动漫动画", "1594985167723864410", "1594985167723864066");
        });
    }

    //批量插入图片
    @Test
    public void test6() {
        String baseUrl = basePathUrl + paths[1];
        List<String> list = getF(baseUrl);
        list.forEach(item -> {
            this.saveNote(baseUrl, item, "1656857358946361346", "头像", "头像", "1594985167723864111", "1594985167723864061");
        });
    }

    @Test
    public void test7() {
            String baseUrl = basePathUrl + paths[2];
            List<String> list = getF(baseUrl);
            list.forEach(item -> {
                this.saveNote(baseUrl, item, "1665003680654012418", "影视", "影视", "1594985167723421069", "1594985167723864065");
            });
    }

    @Test
    public void test8() {
            String baseUrl = basePathUrl + paths[3];
            List<String> list = getF(baseUrl);
            list.forEach(item -> {
                this.saveNote(baseUrl, item, "1601126546037874693", "风景", "风景，景物", "1594985167723864510", "1594985167723864064");
            });
    }

    @Test
    public void test9() {
            String baseUrl = basePathUrl + paths[4];
            List<String> list = getF(baseUrl);
            list.forEach(item -> {
                this.saveNote(baseUrl, item, "1601126546037874690", "壁纸", "壁纸分享", "1594985167723864210", "1594985167723864062");
            });
    }

    public MultipartFile convertMultipartFile(String fileName) {
        File file = new File(fileName);
        MultipartFile multipartFile=null;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(fileName, fileName, ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }



    @Transactional(rollbackFor = Exception.class)
    public void saveNote(String basePath,String fileName11,String userId,String title,String content,String cid,String cpid){
        System.out.println("-------"+fileName11);
        List<MultipartFile> multipartFiles = new ArrayList<>();
        String fileName = basePath+fileName11;
        MultipartFile multipartFile = convertMultipartFile(fileName);
        multipartFiles.add(multipartFile);
        MultipartFile[] files = multipartFiles.toArray(new MultipartFile[multipartFiles.size()]);


        String currentUid = userId;
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCid(cid);
        note.setCpid(cpid);
        note.setCount(1);
        note.setType(1);
        note.setUid(currentUid);
        Integer[] arr = new Integer[]{200,300};
        note.setNoteCoverHeight(RandomUtil.randomEle(arr));
        noteService.save(note);

        // TODO 需要往专辑中添加

        User user = userService.getById(currentUid);
        user.setTrendCount(user.getTrendCount() + 1);
        userService.updateById(user);

        Category category = categoryService.getById(note.getCid());
        Category parentCategory = categoryService.getById(note.getCpid());

        List<String> dataList;
        try {
            Result<List<String>> result = ossClient.saveBatch(files, 1);
            dataList = result.getData();
        }catch (Exception e){
            throw new YanHuoException("添加图片失败");
        }
        String[] urlArr = dataList.toArray(new String[dataList.size()]);
        String urls = JSONUtil.toJsonStr(urlArr);
        note.setUrls(urls);
        note.setNoteCover(urlArr[0]);
        noteService.updateById(note);
        // 往es中添加数据
        NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
        noteSearchVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setLikeCount(0L)
                .setCategoryName(category.getTitle())
                .setCategoryParentName(parentCategory.getTitle())
                .setTags("")
                .setTime(note.getUpdateDate().getTime());
        esClient.addNote(noteSearchVo);
        System.out.println("-------"+fileName11+"----结束");
    }


    public List<String> getF(String basePath){

        List<String> list = new ArrayList<String>();

        File dir = new File(basePath);

        List<File> allFileList = new ArrayList<>();

        // 判断文件夹是否存在
        if (!dir.exists()) {
            System.out.println("目录不存在");
            return null;
        }

        getAllFile(dir, allFileList);

        for (File file : allFileList) {
            System.out.println(file.getName());
            list.add(file.getName());
        }
        System.out.println("该文件夹下共有" + allFileList.size() + "个文件");
        return list;
    }

    public static void getAllFile(File fileInput, List<File> allFileList) {
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, allFileList);
            } else {
                // 如果是文件则将其加入到文件数组中
                allFileList.add(file);
            }
        }
    }

    @Test
    public void test10(){
        String[] a ={"11","22"};
        String jsonStr = JSONUtil.toJsonStr(a);

        JSONArray objects = JSONUtil.parseArray(jsonStr);
        Object[] array = objects.stream().toArray();
        List<String> list = new ArrayList<String>();
        for (Object o : array) {
            System.out.println(o);
            list.add((String) o);
        }

        System.out.println(list);


    }
}
