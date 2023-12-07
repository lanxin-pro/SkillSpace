package cn.iocoder.educate.module.member.service.user;

import cn.iocoder.educate.framework.common.enums.TerminalEnum;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;

/**
 * 会员用户 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 22:29
 */
public interface MemberUserService {

    /**
     * 获得指定会员标签下的用户数量
     *
     * @param tagId 用户标签编号
     * @return 用户数量
     */
    Long getUserCountByTagId(Long tagId);

    /**
     * 获得指定用户分组下的用户数量
     *
     * @param groupId 用户分组编号
     * @return 用户数量
     */
    Long getUserCountByGroupId(Long groupId);

    /**
     * 获得指定用户等级下的用户数量
     *
     * @param levelId 用户等级编号
     * @return 用户数量
     */
    Long getUserCountByLevelId(Long levelId);

    /**
     * 基于手机号创建用户。
     * 如果用户已经存在，则直接进行返回
     *
     * @param mobile     手机号
     * @param registerIp 注册 IP
     * @param terminal   终端 {@link TerminalEnum}
     * @return 用户对象
     */
    MemberUserDO createUserIfAbsent(String mobile, String registerIp, Integer terminal);

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId      用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long userId, String loginIp);

    /**
     * 通过手机查询用户
     *
     * @param mobile 手机
     * @return 用户对象
     */
    MemberUserDO getUserByMobile(String mobile);

}
