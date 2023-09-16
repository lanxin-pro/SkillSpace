package cn.iocoder.springboot.lab28.task.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/16 15:21
 */
@Component
public class DemoJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    @Scheduled(fixedRate = 2000)
    public void execute() {
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
    }

}
