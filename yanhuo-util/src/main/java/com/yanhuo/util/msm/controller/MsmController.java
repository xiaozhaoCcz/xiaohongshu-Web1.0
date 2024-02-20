package com.yanhuo.util.msm.controller;


import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.constant.UserConstant;
import com.yanhuo.util.dto.UserDTO;
import com.yanhuo.util.msm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *发送信息的功能
 * @author xiaozhao
 */
@RestController
@RequestMapping("/util/msm")
public class MsmController {


    @Autowired
    MsmService msmService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 发送短信
     *
     * @param
     * @throws Exception
     */
    @PostMapping("sendMsm")
    public void sendMsm(@RequestBody UserDTO userDTO) throws Exception {

        String code = msmService.sendMsm(userDTO.getPhone());
        redisUtils.set(UserConstant.CODE + userDTO.getPhone(), code, 60 * 5L);
    }
}
