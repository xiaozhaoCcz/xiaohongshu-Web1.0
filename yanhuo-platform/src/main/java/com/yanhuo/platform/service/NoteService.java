package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xiaozhao
 */
public interface NoteService extends IService<Note> {

    /**
     * 根据笔记id获取笔记
     *
     * @param noteId 笔记id
     * @return noteVo
     */
    NoteVo getNoteById(String noteId);

    String saveNoteByDTO(String noteData, MultipartFile[] files);

    /**
     * 删除笔记
     *
     * @param noteIds 笔记id集合
     * @return success
     */
    void deleteNoteByIds(List<String> noteIds);

    /**
     * 更新笔记
     *
     * @param noteDTO 笔记实体
     * @return 笔记id
     */
    void updateNoteByDTO(String noteData, MultipartFile[] files);

    /**
     * 获取热门的笔记数据
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 笔记集合
     */
    Page<NoteVo> getHotPage(long currentPage, long pageSize);

    /**
     * 置顶笔记
     * @param noteId
     * @return
     */
    boolean pinnedNote(String noteId);
}
