package cn.iocoder.educate.module.mp.service.menu;

import cn.iocoder.educate.module.mp.controller.admin.menu.vo.MpMenuSaveReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.menu.MpMenuDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 公众号菜单 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/5 16:25
 */
public interface MpMenuService {

    /**
     * 保存公众号菜单
     *
     * @param createReqVO 创建信息
     */
    void saveMenu(@Valid MpMenuSaveReqVO createReqVO);

    /**
     * 删除公众号菜单
     *
     * @param accountId 公众号账号的编号
     */
    void deleteMenuByAccountId(Long accountId);

    /**
     * 获得公众号菜单列表
     *
     * @param accountId 公众号账号的编号
     * @return 公众号菜单列表
     */
    List<MpMenuDO> getMenuListByAccountId(Long accountId);

}
