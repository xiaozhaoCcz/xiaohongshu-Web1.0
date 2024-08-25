package com.yanhuo.util.oss.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import com.yanhuo.common.utils.SpringContextUtils;
import com.yanhuo.util.oss.factory.OssFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TencentUploadFile implements OssFactory {

    private final String accessKey;

    private final String secretKey;

    private final String bucket;

    private final String region;

    public TencentUploadFile() {
        Environment bean = SpringContextUtils.getBean(Environment.class);
        this.region = bean.getProperty("tencent.region");
        this.accessKey = bean.getProperty("tencent.accessKey");
        this.secretKey = bean.getProperty("tencent.secretKey");
        this.bucket = bean.getProperty("tencent.bucket");
    }

    private COSClient createCosClient() {
        //1.1 初始化用户身份信息
        BasicCOSCredentials credentials = new BasicCOSCredentials(accessKey, secretKey);
        //1.2 设置bucket的地域
        Region region1 = new Region(region);
        ClientConfig clientConfig = new ClientConfig(region1);
        //1.3 生成cos客户端
        return new COSClient(credentials, clientConfig);
    }

    private TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        COSClient cosClient = createCosClient();
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        return transferManager;
    }

    @Override
    public String save(MultipartFile file)  {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
// 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();

// 设置高级接口的配置项
// 分块上传阈值和分块大小分别设置为 5MB 和 1MB（若不特殊设置，分块上传阈值和分块大小的默认值均为5MB）
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5*1024*1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1*1024*1024);
        transferManager.setConfiguration(transferManagerConfiguration);

// 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //1 在文件名称里面添加随机唯一的值
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // yuy76t5rew01.jpg
        fileName = uuid + fileName;

        String datePath = new DateTime().toString("yyyy/MM/dd");
        //拼接
        //  2019/11/12/ewtqr313401.jpg
        fileName = datePath + "/" + fileName;
        try {
            byte[] uploadBytes = file.getBytes();
            InputStream input = new ByteArrayInputStream(uploadBytes);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, input,null);
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForUploadResult();
            // https://yanhuo-1301569504.cos.ap-guangzhou.myqcloud.com/2024%2F08%2F04%2Fe824611855704b098bb5498fd20e8d1d.png
            return "https://"+bucket+".cos."+region+".myqcloud.com/"+fileName;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
// 确定本进程不再使用 transferManager 实例之后，关闭即可
// 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);
        return null;
    }

    private void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);

    }


    @Override
    public Boolean delete(String path) {
        COSClient cosClient = createCosClient();
        try {
            cosClient.deleteObject(bucket, path);
            return true;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
