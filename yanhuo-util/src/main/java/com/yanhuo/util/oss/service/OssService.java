package com.yanhuo.util.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String save(MultipartFile file, Integer type);
}
