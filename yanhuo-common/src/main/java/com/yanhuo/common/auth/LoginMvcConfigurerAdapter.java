package com.yanhuo.common.auth;
import com.yanhuo.common.constant.UploadFileConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiaozhao
 */
@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurationSupport{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .excludePathPatterns("/**/auth/**", "/**/util/**", "/**/search/**","/**/oss/**","/**/category/getCategoryTreeData")
                .addPathPatterns("/**");

        super.addInterceptors(registry);
    }

    // 添加静态资源访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(UploadFileConstant.OSS+"/**") //虚拟url路径
                .addResourceLocations("file:"+UploadFileConstant.ADDRESS); //真实本地路径
        super.addResourceHandlers(registry);
    }
}
