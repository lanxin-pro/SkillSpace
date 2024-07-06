package cn.iocoder.educate.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: j-sentinel
 * @Date: 2023/4/26 21:43
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${lanxin.info.base-package}
@SpringBootApplication(scanBasePackages = {
        "${lanxin.info.base-package}.server",
        "${lanxin.info.base-package}.module",
        "com.xingyuv.captcha.config"
})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
