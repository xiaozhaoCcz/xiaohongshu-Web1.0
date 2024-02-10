package com.yanhuo.platform.client;

import com.yanhuo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value = "util",url = "http://localhost:8805")
@Component
public interface OssClient {

    /**
     * 远程调用上传文件服务（consumes = MediaType.MULTIPART_FORM_DATA_VALUE）
     * @param files
     * @param type
     * @return
     */
    @RequestMapping(value = "/util/oss/saveBatch/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<List<String>> saveBatch(@RequestPart("uploadFiles") MultipartFile[] files, @PathVariable Integer type);
}
