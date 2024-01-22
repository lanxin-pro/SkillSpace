package cn.iocoder.educate.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.object.PageUtils;
import cn.iocoder.educate.module.infra.api.file.FileApi;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserUpdateReqVO;
import cn.iocoder.educate.module.system.convert.user.UserConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.UserPostDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.post.UserPostMapper;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import cn.iocoder.educate.module.system.service.post.PostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private PostService postService;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private PermissionService permissionService;

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
        return adminUserMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()));
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

    @Override
    public void updateUserPassword(Long id, String password) {
        // 校验用户存在
        validateUserExists(id);
        // 更新密码
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(id);
        // 加密密码
        adminUserDO.setPassword(encodePassword(password));
        adminUserMapper.updateById(adminUserDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateReqVO userCreateReqVO) {
        // TODO j-sentinel 添加的时候考虑租户功能
        // 校验正确性
        validateUserForCreateOrUpdate(null,userCreateReqVO.getUsername(),userCreateReqVO.getMobile(),
                userCreateReqVO.getEmail(),userCreateReqVO.getDeptId(),userCreateReqVO.getPostIds());
        // 对象转化
        AdminUserDO user = UserConvert.INSTANCE.convert(userCreateReqVO);
        // 默认开启
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        // 加密密码
        user.setPassword(encodePassword(userCreateReqVO.getPassword()));
        adminUserMapper.insert(user);
        // 插入关联岗位(前端传递过来的PostIds)
        if (CollectionUtil.isNotEmpty(user.getPostIds())) {
            List<UserPostDO> userPostCollect = user.getPostIds()
                    .stream()
                    // Map的对象是UserPostDO
                    .map(postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            userPostMapper.insertBatch(userPostCollect);
        }
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateReqVO userUpdateReqVO) {
        // 校验正确性
        validateUserForCreateOrUpdate(userUpdateReqVO.getId(), userUpdateReqVO.getUsername(),
                userUpdateReqVO.getMobile(), userUpdateReqVO.getEmail(), userUpdateReqVO.getDeptId(),
                userUpdateReqVO.getPostIds());
        // 更新用户
        AdminUserDO adminUserDO = UserConvert.INSTANCE.convert(userUpdateReqVO);
        adminUserMapper.updateById(adminUserDO);
        // 更新岗位 这个方法主要是维护中间表的操作
        updateUserPost(userUpdateReqVO, adminUserDO);
    }

    @Override
    public void deleteUser(Long id) {
        // 校验用户存在
        validateUserExists(id);
        // 删除用户
        adminUserMapper.deleteById(id);
        // 删除用户关联数据
        permissionService.processUserDeleted(id);
        // 删除用户岗位
        userPostMapper.deleteByUserId(id);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 校验用户存在
        validateUserExists(id);
        // 更新状态
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(id);
        adminUserDO.setStatus(status);
        adminUserMapper.updateById(adminUserDO);
    }

    @Override
    public List<AdminUserDO> getUserListByStatus(Integer status) {
        return adminUserMapper.selectListByStatus(status);
    }

    @Override
    public String getUserNickname(Long id) {
        return adminUserMapper.selectByNickname(id);
    }

    /**
     * 相对于数据库如果我多了一个岗位的话，我就应该添加，相对的如果我少了一个的话。就需要删除
     *
     * @param userUpdateReqVO
     * @param adminUserDO
     */
    private void updateUserPost(UserUpdateReqVO userUpdateReqVO, AdminUserDO adminUserDO) {
        Long userId = userUpdateReqVO.getId();
        // 用户岗位中间表
        List<UserPostDO> userPostDOS = userPostMapper.selectListByUserId(userId);
        // 岗位ids
        Set<Long> dbPostIds = userPostDOS.stream()
                .map(UserPostDO::getPostId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 计算新增和删除的岗位编号
        Set<Long> postIds = adminUserDO.getPostIds();
        // 数据库和前端传递的，返回集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素 添加
        Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
        // 数据库和前端传递的，返回集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素 删除
        Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (CollectionUtil.isNotEmpty(createPostIds)) {
            List<UserPostDO> userPostCollect = createPostIds.stream()
                    // postId就是我现在要添加的
                    .map(postId -> new UserPostDO().setUserId(userId).setPostId(postId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            userPostMapper.insertBatch(userPostCollect);
        }
        if (CollectionUtil.isNotEmpty(deletePostIds)) {
            // 等于userId的记录，并且删除包含deletePostIds的元素
            userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
        }
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

    private void validateUserForCreateOrUpdate(Long id, String username, String mobile, String email,
                                               Long deptId, Set<Long> postIds) {
        // 校验用户存在
        validateUserExists(id);
        // 校验用户名唯一
        validateUsernameUnique(id, username);
        // 校验手机号唯一
        validateMobileUnique(id, mobile);
        // 校验邮箱唯一
        validateEmailUnique(id, email);
        // 校验部门处于开启状态
        deptService.validateDeptList(Collections.singleton(deptId));
        // 校验岗位处于开启状态
        postService.validatePostList(postIds);
    }

    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        AdminUserDO user = adminUserMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
    }

    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            return;
        }
        AdminUserDO user = adminUserMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
    }

    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isEmpty(username)) {
            return;
        }
        AdminUserDO user = adminUserMapper.selectByUsername(username);
        if(user == null){
            return;
        }
        // 对比完昵称，开始对比是否存在相同的id+用户名
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
