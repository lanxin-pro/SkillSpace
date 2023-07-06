package cn.iocoder.educate.module.system.service.member;

/**
 * Member Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/6 14:15
 */
public interface MemberService {

    /**
     * 获得会员用户的手机号码
     *
     * @param userId 会员用户编号
     * @return 手机号码
     */
    String getMemberUserMobile(Long userId);
}
