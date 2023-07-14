package cn.iocoder.educate.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserProfileRespVO;
import cn.iocoder.educate.module.system.convert.user.UserConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import cn.iocoder.educate.module.system.service.permission.RoleService;
import cn.iocoder.educate.module.system.service.post.PostService;
import cn.iocoder.educate.module.system.service.social.SocialUserService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/10 13:13
 */
@Tag(name = "管理后台 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private DeptService deptService;

    @Resource
    private PostService postService;

    @Resource
    private SocialUserService socialUserService;

    @GetMapping("/get")
    @Operation(summary = "获得登录用户信息")
    @OperateLog(enable = true)
    public CommonResult<UserProfileRespVO> profile() {
        // 获得用户基本信息
        AdminUserDO user = adminUserService.getUser(SecurityFrameworkUtils.getLoginUserId());
        UserProfileRespVO userProfileRespVO = UserConvert.INSTANCE.convert03(user);
        // 获得用户的角色ids
        Set<Long> userRoleIdListByUserId = permissionService.getUserRoleIdListByUserId(user.getId());
        // 获得用户角色
        List<RoleDO> roleListFromCache = roleService.getRoleListFromCache(userRoleIdListByUserId);
        userProfileRespVO.setRoles(UserConvert.INSTANCE.convertList(roleListFromCache));
        // 获得部门信息
        if(user.getDeptId() != null) {
            DeptDO dept = deptService.getDept(user.getDeptId());
            userProfileRespVO.setDept(UserConvert.INSTANCE.convert02(dept));
        }
        // 获得岗位信息
        if(CollUtil.isNotEmpty(user.getPostIds())){
            List<PostDO> postList = postService.getPostList(user.getPostIds());
            userProfileRespVO.setPosts(UserConvert.INSTANCE.convertList02(postList));
        }
        // 获得社交用户信息
        List<SocialUserDO> socialUsers = socialUserService.getSocialUserList(user.getId(), UserTypeEnum.ADMIN.getValue());
        userProfileRespVO.setSocialUsers(UserConvert.INSTANCE.convertList03(socialUsers));
        return CommonResult.success(userProfileRespVO);
    }

    /**
     * 解决 uni-app 不支持 Put 上传文件的问题
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/update-avatar", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_IS_EMPTY);
        }
        String avatar = adminUserService.updateUserAvatar(SecurityFrameworkUtils.getLoginUserId(),file.getInputStream());
        return CommonResult.success(avatar);
    }
}
