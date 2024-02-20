package com.yanhuo.util.oss.service.impl;

import com.yanhuo.common.constant.UploadFileConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.util.oss.factory.OssFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaozhao
 */
@Slf4j
public class UploadFileToLoacl implements OssFactory {

    @Override
    public String save(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new YanHuoException("文件不能为空");
            }
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String yearPath = yearFormat.format(new Date());
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            String monthPath = monthFormat.format(new Date());
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            String dayPath = dayFormat.format(new Date());
            String folderPath = yearPath + "/" + monthPath + "/" + dayPath;
            File directory = new File(UploadFileConstant.ADDRESS + folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String orgName = file.getOriginalFilename();
            String suffixName = orgName.substring(orgName.lastIndexOf("."));
            String fileName;
            if (orgName.contains(".")) {
                fileName = System.currentTimeMillis() + suffixName;
            } else {
                throw new YanHuoException("文件类型错误");
            }
            Path destination = Paths.get(UploadFileConstant.ADDRESS, folderPath, fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING); // 保存文件
            return UploadFileConstant.OSS + folderPath + "/" + fileName;
        } catch (Exception e) {
            throw new YanHuoException("文件上传错误");
        }
    }

    @Override
    public Boolean delete(String path) {
        String prePath = UploadFileConstant.ADDRESS;
        String[] split = path.split(UploadFileConstant.OSS);
        String filePath = prePath + split[1];
        log.info("filePath:{}", filePath);
        File file = new File(filePath);
        if (file.exists()) {//文件是否存在
            return file.delete();
        }
        return false;
    }
}
