package cn.iocoder.educate.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.file.FileApi;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
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

    @Resource
    private FileApi fileApi;

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

    @Override
    public List<AdminUserDO> getUserListByNickname(String userNickname) {
        return adminUserMapper.selectListByNickname(userNickname);
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            return Collections.emptyList();
        }
        return adminUserMapper.selectBatchIds(ids);
    }

    @Override
    public String updateUserAvatar(Long loginUserId, InputStream avatarFile) {
        // 校验用户id是否存在
        validateUserExists(loginUserId);
        // 存储文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新用户图像的路径
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(loginUserId);
        adminUserDO.setAvatar(avatar);
        adminUserMapper.updateById(adminUserDO);
        return avatar;
    }

    private void validateUserExists(Long loginUserId) {
        if(loginUserId == null){
            return;
        }
        AdminUserDO user = adminUserMapper.selectById(loginUserId);
        if(user == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
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
