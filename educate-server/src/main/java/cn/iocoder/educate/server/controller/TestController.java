package cn.iocoder.educate.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: j-sentinel
 * @Date: 2023/4/26 21:50
 */
@RestController
public class TestController {

    @GetMapping("/h1")
    public String test(){
        return "HelloWorld";
    }
}
