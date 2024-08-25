package com.yanhuo.util.oss.service.impl;

import com.yanhuo.util.oss.factory.OssFactory;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaozhao
 */
@Service
public class OssServiceImpl implements OssService {

    @Override
    public String save(MultipartFile file, Integer type) {
        OssFactory factory = null;
        switch (type) {
            case 0:
                // 本地上传图片
                factory = new UploadFileToLoacl();
                break;
            case 1:
                factory = new QiNiuYunUploadFile();
                break;
            case 2:
                factory = new TencentUploadFile();
                break;
            default:
                break;
        }
        if (factory != null) {
            return factory.save(file);
        }
        return null;
    }

    @Override
    public List<String> saveBatch(MultipartFile[] files, Integer type) {
        List<String> result = new ArrayList<>();
        // 需要进行加锁，不然会出现多次添加
        for (MultipartFile file : files) {
            result.add(save(file, type));
        }
        return result;
    }

    @Override
    public void delete(String path, Integer type) {
        OssFactory factory = null;
        switch (type) {
            case 0:
                // 本地上传图片
                factory = new UploadFileToLoacl();
                break;
            case 1:
                factory = new QiNiuYunUploadFile();
                break;
            case 2:
                factory = new TencentUploadFile();
                break;
            default:
                break;
        }
        factory.delete(path);
    }

    @Override
    public void batchDelete(List<String> filePaths, Integer type) {
        for (String path : filePaths) {
            delete(path, type);
        }
    }
}
