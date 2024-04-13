package com.yanhuo.platform.client;

import com.yanhuo.common.im.Message;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.config.FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 解决远程调用设置token
 * @author xiaozhao
 */
@FeignClient(value = "im",url = "http://localhost:8802",configuration = {FeignRequestInterceptor.class})
@Component
public interface ChatClient {
    /**
     * 发送消息
     *
     * @param message 消息实体
     * @return success
     */
    @PostMapping("/im/chat/sendMsg")
    Result<?> sendMsg(@RequestBody Message message);
}
