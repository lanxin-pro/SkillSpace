package cn.iocoder.educate.module.system.api.user;

import cn.iocoder.educate.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.educate.module.system.convert.user.UserConvert;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Admin 用户 API 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/24 17:33
 */
@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService adminUserService;

    @Override
    public AdminUserRespDTO getUser(Long id) {
        AdminUserDO user = adminUserService.getUser(id);
        return UserConvert.INSTANCE.convert4(user);
    }

}
