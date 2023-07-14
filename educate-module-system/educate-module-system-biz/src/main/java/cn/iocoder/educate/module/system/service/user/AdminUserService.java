package cn.iocoder.educate.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/9 8:44
 * 后台用户 Service 接口
 */
public interface AdminUserService {

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId 用户编号
     * @param clientIP 登陆 IP
     */
    void updateUserLogin(Long userId, String clientIP);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    AdminUserDO getUserByUsername(String username);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    /**
     * 通过手机号获取用户
     *
     * @param mobile
     * @return
     */
    AdminUserDO getUserByMobile(String mobile);

    /**
     * 通过用户 ID 查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    AdminUserDO getUser(Long userId);

    /**
     * 获得用户分页列表
     *
     * @param reqVO 分页条件
     * @return 分页列表
     */
    PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO);

    /**
     * 获得用户列表，基于昵称模糊匹配
     *
     * @param userNickname 昵称
     * @return 用户列表
     */
    List<AdminUserDO> getUserListByNickname(String userNickname);

    /**
     * 获得用户列表
     *
     * @param ids 用户编号数组
     * @return 用户列表
     */
    List<AdminUserDO> getUserList(Collection<Long> ids);

    /**
     * 获得用户 Map
     *
     * @param userIds 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, AdminUserDO> getUserMap(List<Long> userIds){
        if(CollUtil.isEmpty(userIds)){
            return new HashMap<>();
        }
        return getUserList(userIds)
                .stream()
                .collect(Collectors.toMap(AdminUserDO::getId, Function.identity(), (v1,v2) -> v1));
    }

    /**
     * 更新用户头像
     *
     * @param loginUserId 用户 id
     * @param inputStream 头像文件
     * @return
     */
    String updateUserAvatar(Long loginUserId, InputStream inputStream);
}
