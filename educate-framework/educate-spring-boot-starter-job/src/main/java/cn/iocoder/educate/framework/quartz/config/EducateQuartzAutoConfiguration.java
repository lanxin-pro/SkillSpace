package cn.iocoder.educate.framework.quartz.config;

import cn.iocoder.educate.framework.quartz.core.scheduler.SchedulerManager;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:22
 */
@EnableScheduling // 开启 Spring 自带的定时任务
@Configuration
public class EducateQuartzAutoConfiguration {

    @Bean
    public SchedulerManager schedulerManager(Scheduler scheduler) {
        return new SchedulerManager(scheduler);
    }

}
