package com.yanhuo.platform.client;

import com.yanhuo.common.im.Message;
import com.yanhuo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "im",url = "http://localhost:8802")
@Component
public interface ChatCliient {

    @RequestMapping("/im/chat/sendMsg")
    Result<?> sendMsg(@RequestBody Message message);
}
