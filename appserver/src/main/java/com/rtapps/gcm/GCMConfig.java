package com.rtapps.gcm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by rtichauer on 7/30/16.
 */
@Configuration
@ComponentScan(basePackages = "com.rtapps.gcm")
public class GCMConfig {
    @Bean
    public ThreadPoolTaskExecutor gcmTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(10);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

}