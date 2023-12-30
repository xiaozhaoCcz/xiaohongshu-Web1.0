package com.yanhuo.common.auth;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author xiaozhao
 */
@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                 .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**","/api/util/**","/api/search/**");
        super.addInterceptors(registry);
    }
}
