package com.yanhuo.xo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.entity.Note;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaozhao
 */
@Mapper
public interface NoteDao extends BaseMapper<Note> {
}
