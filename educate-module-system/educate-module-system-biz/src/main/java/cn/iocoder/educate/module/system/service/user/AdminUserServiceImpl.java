package cn.iocoder.educate.module.system.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:44
 * 后台用户 Service 实现类
 */
@Slf4j
@Service
@Validated
public class AdminUserServiceImpl implements AdminUserService{
    @Override
    public void updateUserLogin(Long userId, String clientIP) {

    }
}
