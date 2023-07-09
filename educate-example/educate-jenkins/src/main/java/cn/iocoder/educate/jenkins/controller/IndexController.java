package cn.iocoder.educate.jenkins.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 董伟豪
 * @Date: 2023/1/4 11:39
 */
@RestController
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(){
        log.info("测试jenkins");
        return "我是jenkins！！！我开始发版了，jenkins自动化测试！！！";
    }
}
