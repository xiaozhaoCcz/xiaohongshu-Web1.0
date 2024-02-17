package com.yanhuo.util.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xiaozhao
 */
public interface OssService {
    String save(MultipartFile file, Integer type);

    List<String> saveBatch(MultipartFile[] files, Integer type);

    void delete(String path, Integer type);

    void batchDelete(List<String> filePaths, Integer type);
}
