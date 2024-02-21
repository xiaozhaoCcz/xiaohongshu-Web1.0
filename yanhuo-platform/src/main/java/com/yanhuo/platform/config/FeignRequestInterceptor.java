package com.yanhuo.platform.config;

import com.yanhuo.common.constant.TokenConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author xiaozhao
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final String[] WHITE_METHODS = {"addNoteBulkData"};

    @Override
    public void apply(RequestTemplate template) {
        String methodName = template.methodMetadata().method().getName();
        if (Arrays.asList(WHITE_METHODS).contains(methodName)) {
            return;
        }

        // 从header获取X-token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attr = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attr.getRequest();
        String token = request.getHeader(TokenConstant.ACCESS_TOKEN);//网关传过来的 token
        if (StringUtils.hasText(token)) {
            template.header(TokenConstant.ACCESS_TOKEN, token);
        }
    }
}
