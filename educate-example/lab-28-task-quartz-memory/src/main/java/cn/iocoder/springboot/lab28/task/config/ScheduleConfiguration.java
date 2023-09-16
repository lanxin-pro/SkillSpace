package cn.iocoder.springboot.lab28.task.config;

import cn.iocoder.springboot.lab28.task.config.job.DemoJob01;
import cn.iocoder.springboot.lab28.task.config.job.DemoJob02;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/16 16:34
 */
@Configuration
public class ScheduleConfiguration {

    public static class DemoJob01Configuration {

        @Bean
        public JobDetail demoJob01() {
            return JobBuilder.newJob(DemoJob01.class)
                    // 名字为 demoJob01
                    .withIdentity("demoJob01")
                    // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
                    .storeDurably()
                    .build();
        }

        @Bean
        public Trigger demoJob01Trigger() {
            // 简单的调度计划的构造器
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    // 频率。
                    .withIntervalInSeconds(5)
                    // 次数。
                    .repeatForever();
            // Trigger 构造器
            return TriggerBuilder.newTrigger()
                    // 对应 Job 为 demoJob01
                    .forJob(demoJob01())
                    // 名字为 demoJob01Trigger
                    .withIdentity("demoJob01Trigger")
                    // 对应 Schedule 为 scheduleBuilder
                    .withSchedule(scheduleBuilder)
                    .build();
        }

    }

    public static class DemoJob02Configuration {

        @Bean
        public JobDetail demoJob02() {
            return JobBuilder.newJob(DemoJob02.class)
                    // 名字为 demoJob02
                    .withIdentity("demoJob02")
                    // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
                    .storeDurably()
                    .build();
        }

        @Bean
        public Trigger demoJob02Trigger() {
            // 基于 Quartz Cron 表达式的调度计划的构造器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");
            // Trigger 构造器
            return TriggerBuilder.newTrigger()
                    // 对应 Job 为 demoJob02
                    .forJob(demoJob02())
                    // 名字为 demoJob02Trigger
                    .withIdentity("demoJob02Trigger")
                    // 对应 Schedule 为 scheduleBuilder
                    .withSchedule(scheduleBuilder)
                    .build();
        }

    }

}
