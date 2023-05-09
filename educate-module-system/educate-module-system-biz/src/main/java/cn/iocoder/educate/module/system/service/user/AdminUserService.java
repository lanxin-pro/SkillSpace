package cn.iocoder.educate.module.system.service.user;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:44
 * 后台用户 Service 接口
 */
public interface AdminUserService {

    void updateUserLogin(Long userId, String clientIP);
}
