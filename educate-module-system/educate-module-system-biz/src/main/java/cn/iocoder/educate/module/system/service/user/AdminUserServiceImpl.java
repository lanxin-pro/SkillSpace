package cn.iocoder.educate.module.system.service.user;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Resource
    private DeptService deptService;

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

    @Override
    public AdminUserDO getUserByMobile(String mobile) {
        LambdaQueryWrapper<AdminUserDO> adminUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminUserDOLambdaQueryWrapper.eq(AdminUserDO::getMobile,mobile);
        AdminUserDO adminUserDO = adminUserMapper.selectOne(adminUserDOLambdaQueryWrapper);
        return adminUserDO;
    }

    @Override
    public AdminUserDO getUser(Long userId) {
        return adminUserMapper.selectById(userId);
    }

    @Override
    public PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO) {
        // 分页的pageNo pageSize      分页条件(这个是我想要查询的ids)
        return adminUserMapper.selectPage(reqVO,getDeptCondition(reqVO.getDeptId()));
    }

    /**
     * 获得部门条件：查询指定部门的子部门编号们，包括自身
     *
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = deptService.getDeptListByParentIdFromCache(deptId, true)
                .stream()
                .map(DeptDO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 包括自身(自身有可能是最低层的列表)
        deptIds.add(deptId);
        return deptIds;
    }
}
