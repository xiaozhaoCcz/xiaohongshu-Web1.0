package com.yanhuo.platform.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class XxlJobConfig {

    @Value("${yanhuo.admin.addresses}")
    String adminAddresses;
    @Value("${yanhuo.executor.appname}")
    String appname;
    @Value("${yanhuo.accessToken}")
    String accessToken;

    @Value("${yanhuo.executor.ip}")
    String ip;
    @Value("${yanhuo.executor.port}")
    Integer port;
    @Value("${yanhuo.executor.logpath}")
    String logpath;
    @Value("${yanhuo.executor.logretentiondays}")
    Integer logretentiondays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logpath);
        xxlJobSpringExecutor.setLogRetentionDays(logretentiondays);

        return xxlJobSpringExecutor;
    }
}
