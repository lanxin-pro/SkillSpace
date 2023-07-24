package cn.iocoder.educate.module.system.api.user;

import cn.iocoder.educate.module.system.api.user.dto.AdminUserRespDTO;

/**
 * Admin 用户 API 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/24 17:28
 */
public interface AdminUserApi {

    AdminUserRespDTO getUser(Long id);

}
