package cn.iocoder.educate.module.mp.service.account;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;

/**
 * 公众号账号 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 11:40
 */
public interface MpAccountService {

    /**
     * 初始化缓存
     */
    void initLocalCache();

    /**
     * 获得公众号账号分页
     *
     * @param pageReqVO 分页查询
     * @return 公众号账号分页
     */
    PageResult<MpAccountDO> getAccountPage(MpAccountPageReqVO pageReqVO);

}
