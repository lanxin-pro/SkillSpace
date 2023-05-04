package cn.iocoder.educate.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: j-sentinel
 * @Date: 2023/4/26 21:50
 */
@RestController
@Tag(name = "测试接口")
public class TestController {

    @GetMapping("/h1")
    @Operation(summary = "测试")
    public String test(){
        return "HelloWorld";
    }
}
