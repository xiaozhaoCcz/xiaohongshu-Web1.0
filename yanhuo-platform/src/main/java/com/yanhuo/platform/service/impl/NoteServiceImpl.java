package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.entity.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {
}
