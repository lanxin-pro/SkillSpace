package cn.iocoder.educate.module.member.service.user;

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

}
