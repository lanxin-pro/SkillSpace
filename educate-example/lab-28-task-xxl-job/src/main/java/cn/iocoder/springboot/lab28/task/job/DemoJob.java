package cn.iocoder.springboot.lab28.task.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/16 16:54
 */
@Component
public class DemoJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        // 打印日志
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
    }

}
