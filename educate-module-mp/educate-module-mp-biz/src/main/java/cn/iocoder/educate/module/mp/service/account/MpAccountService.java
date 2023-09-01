package cn.iocoder.educate.module.mp.service.account;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountCreateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountUpdateReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;

import javax.validation.Valid;
import java.util.List;

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
     * 创建公众号账号
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAccount(@Valid MpAccountCreateReqVO createReqVO);

    /**
     * 更新公众号账号
     *
     * @param updateReqVO 更新信息
     */
    void updateAccount(@Valid MpAccountUpdateReqVO updateReqVO);

    /**
     * 删除公众号账号
     *
     * @param id 编号
     */
    void deleteAccount(Long id);

    /**
     * 获得公众号账号
     *
     * @param id 编号
     * @return 公众号账号
     */
    MpAccountDO getAccount(Long id);

    /**
     * 获得公众号账号分页
     *
     * @param pageReqVO 分页查询
     * @return 公众号账号分页
     */
    PageResult<MpAccountDO> getAccountPage(MpAccountPageReqVO pageReqVO);

    /**
     * 生成公众号账号的二维码
     *
     * @param id 编号
     */
    void generateAccountQrCode(Long id);

    /**
     * 获得公众号账号列表
     *
     * @return 公众号账号列表
     */
    List<MpAccountDO> getAccountList();

}
