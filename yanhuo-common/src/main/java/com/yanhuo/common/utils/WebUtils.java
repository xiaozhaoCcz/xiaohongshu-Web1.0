package com.yanhuo.common.utils;

import com.yanhuo.common.constant.TokenConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xiaozhao
 * web端工具类
 */
@Slf4j
public class WebUtils {
    private WebUtils() {}
    public static String getRequestHeader(String headerName){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(ra)){
            log.error("服务里RequestAttributes对象为空");
            return null;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request.getHeader(headerName);
    }
}
