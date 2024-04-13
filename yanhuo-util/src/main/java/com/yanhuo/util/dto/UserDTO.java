package com.yanhuo.util.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xiaozhao
 */
@Data
public class UserDTO implements Serializable {
    //@Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式有误", groups = DefaultGroup.class)
    private String phone;

    //@Pattern(regexp = "^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$", message = "邮箱输入有误", groups = DefaultGroup.class)
    private String email;
}
