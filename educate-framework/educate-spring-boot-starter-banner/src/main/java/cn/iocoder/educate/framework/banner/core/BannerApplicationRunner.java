package cn.iocoder.educate.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import java.util.concurrent.TimeUnit;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/2 23:37
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 直接在公共线程池中执行线程
        ThreadUtil.execute( () -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" +
                            "接口文档: \t{} \n\t" +
                            "开发文档: \t{} \n\t" +
                            "视频教程: \t{} \n\t" +
                            "源码解析: \t{} \n" +
                            "----------------------------------------------------------",
                    "https://doc.lanxin.cn/api-doc/",
                    "https://doc.lanxin.cn",
                    "https://t.lanxin.com/78952",
                    "https://t.lanxin.com/00215");
        });
    }
}
