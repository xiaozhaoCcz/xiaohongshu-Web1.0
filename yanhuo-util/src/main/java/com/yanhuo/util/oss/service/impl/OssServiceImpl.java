package com.yanhuo.util.oss.service.impl;

import com.yanhuo.util.oss.factory.OssFactory;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String save(MultipartFile file, Integer type) {
        OssFactory factory = null;
        switch (type) {
            case 0:
                // 本地上传图片
                factory =   new UploadFileToLoacl();
                break;
            case 1:
                break;
            default:
                break;
        }
        if(factory!=null){
           return factory.save(file);
        }
        return null;
    }
}
