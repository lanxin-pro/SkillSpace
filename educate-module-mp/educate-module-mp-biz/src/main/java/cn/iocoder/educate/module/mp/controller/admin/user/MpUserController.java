package cn.iocoder.educate.module.mp.controller.admin.user;

import cn.iocoder.educate.module.mp.service.user.MpUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:28
 */
@Tag(name = "管理后台 - 公众号粉丝")
@RestController
@RequestMapping("/mp/user")
@Validated
public class MpUserController {

    @Resource
    private MpUserService mpUserService;

}
