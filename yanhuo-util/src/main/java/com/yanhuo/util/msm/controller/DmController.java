package com.yanhuo.util.msm.controller;

import cn.hutool.core.util.RandomUtil;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.constant.UserConstant;
import com.yanhuo.util.dto.UserDTO;
import com.yanhuo.util.msm.service.DmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *发送邮件的功能
 * @author xiaozhao
 */
@RestController
@RequestMapping("/dm")
public class DmController {

    @Autowired
    DmService dmService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 发生邮件
     *
     * @param
     * @return
     */
    @PostMapping("sendDm")
    public Result<?> sendDm(@RequestBody UserDTO userDTO) {
        try {
            String content = RandomUtil.randomNumbers(4);
            dmService.sendDm(userDTO.getEmail(), content);
            redisUtils.set(UserConstant.CODE + userDTO.getEmail(), content, 60 * 5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }
}
