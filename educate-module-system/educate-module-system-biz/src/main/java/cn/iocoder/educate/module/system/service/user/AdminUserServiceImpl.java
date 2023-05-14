package cn.iocoder.educate.module.system.service.user;

import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:44
 * 后台用户 Service 实现类
 */
@Slf4j
@Service
@Validated
public class AdminUserServiceImpl implements AdminUserService{

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateUserLogin(Long userId, String clientIP) {
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(userId)
                .setLoginIp(clientIP)
                .setLoginDate(LocalDateTime.now());
        adminUserMapper.updateById(adminUserDO);
    }

    @Override
    public AdminUserDO getUserByUsername(String username) {
        LambdaQueryWrapper<AdminUserDO> adminUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminUserDOLambdaQueryWrapper.eq(AdminUserDO::getUsername,username);
        return adminUserMapper.selectOne(adminUserDOLambdaQueryWrapper);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
}
