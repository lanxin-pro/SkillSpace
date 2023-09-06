package cn.iocoder.educate.module.mp.service.user;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;

/**
 * 公众号粉丝 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:29
 */
public interface MpUserService {

    /**
     * 获得公众号粉丝分页
     *
     * @param mpUserPageReqVO 分页查询
     * @return 公众号粉丝分页
     */
    PageResult<MpUserDO> getUserPage(MpUserPageReqVO mpUserPageReqVO);

    /**
     * 获得公众号粉丝
     *
     * @param id 编号
     * @return 公众号粉丝
     */
    MpUserDO getUser(Long id);

    /**
     * 使用 appId + openId，获得公众号粉丝
     *
     * @param appId 公众号 appId
     * @param openId 公众号 openId
     * @return 公众号粉丝
     */
    MpUserDO getUser(String appId, String openId);

    /**
     * 更新公众号粉丝
     *
     * @param updateReqVO 更新信息
     */
    void updateUser(MpUserUpdateReqVO updateReqVO);

    /**
     * 同步一个公众号粉丝
     *
     * @param accountId 公众号账号的编号
     */
    void syncUser(Long accountId);

}
