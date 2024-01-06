package com.yanhuo.util.oss.factory;

import org.springframework.web.multipart.MultipartFile;

public interface OssFactory {

    public String save(MultipartFile file);
}
