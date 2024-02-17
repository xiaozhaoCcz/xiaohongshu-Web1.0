package com.yanhuo.common.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * token常量
 * @author xiaozhao
 */
@ApiModel("token常量")
public interface TokenConstant {

    @ApiModelProperty("accessToken")
    String ACCESS_TOKEN = "accessToken";

    @ApiModelProperty("refreshToken")
    String REFRESH_TOKEN= "refreshToken";
}
