package com.yanhuo.util.oss.factory;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiaozhao
 */
public interface OssFactory {

    public String save(MultipartFile file);

    public Boolean delete(String path);
}
