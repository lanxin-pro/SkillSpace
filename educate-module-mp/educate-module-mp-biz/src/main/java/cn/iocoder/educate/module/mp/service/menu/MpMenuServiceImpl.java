package cn.iocoder.educate.module.mp.service.menu;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.mp.controller.admin.menu.vo.MpMenuSaveReqVO;
import cn.iocoder.educate.module.mp.convert.menu.MpMenuConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.menu.MpMenuDO;
import cn.iocoder.educate.module.mp.dal.mysql.menu.MpMenuMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.framework.mp.core.util.MpUtils;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/5 16:25
 */
@Slf4j
@Service
@Validated
public class MpMenuServiceImpl implements MpMenuService {

    @Resource
    private MpMenuMapper mpMenuMapper;

    @Resource
    private MpAccountService mpAccountService;

    @Resource
    private MpServiceFactory mpServiceFactory;

    @Resource
    private Validator validator;
    // TODO j-sentinel 测试发现这里 @Transactional不生效与公众号的添加
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(MpMenuSaveReqVO createReqVO) {
        // 获取到账号
        MpAccountDO account = mpAccountService.getRequiredAccount(createReqVO.getAccountId());
        // api
        WxMpService mpService = mpServiceFactory.getRequiredMpService(createReqVO.getAccountId());

        // 参数校验（需要添加的菜单列表）
        // TODO 优化
        // for (MpMenuSaveReqVO.Menu a : createReqVO.getMenus())
        /*
        createReqVO.getMenus().forEach(menu -> {
            validateMenu(menu);
        });
        */
        createReqVO.getMenus().forEach(this::validateMenu);

        // 第一步，同步公众号
        WxMenu wxMenu = new WxMenu();
        wxMenu.setButtons(MpMenuConvert.INSTANCE.convert(createReqVO.getMenus()));
        try {
            mpService.getMenuService().menuCreate(wxMenu);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_SAVE_FAIL, e.getError().getErrorMsg());
        }

        // 第二步，存储到数据库
        mpMenuMapper.deleteByAccountId(createReqVO.getAccountId());
        createReqVO.getMenus().forEach(menu -> {
            // 先保存顶级菜单
            MpMenuDO menuDO = createMenu(menu, null, account);
            // 再保存子菜单
            if (CollUtil.isEmpty(menu.getChildren())) {
                return;
            }
            menu.getChildren().forEach(childMenu -> {
                createMenu(childMenu, menuDO, account);
            });
        });
    }

    @Override
    public void deleteMenuByAccountId(Long accountId) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        // 第一步，同步公众号
        try {
            mpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_DELETE_FAIL, e.getError().getErrorMsg());
        }

        // 第二步，存储到数据库
        mpMenuMapper.deleteByAccountId(accountId);
    }

    @Override
    public List<MpMenuDO> getMenuListByAccountId(Long accountId) {
        return mpMenuMapper.selectListByAccountId(accountId);
    }

    /**
     * 创建菜单，并存储到数据库
     *
     * @param wxMenu 菜单信息
     * @param parentMenu 父菜单
     * @param account 公众号账号
     * @return 创建后的菜单
     */
    private MpMenuDO createMenu(MpMenuSaveReqVO.Menu wxMenu, MpMenuDO parentMenu, MpAccountDO account) {
        // 创建菜单
        // 子菜单不为空setName
        MpMenuDO menu = CollUtil.isNotEmpty(wxMenu.getChildren())
                ? new MpMenuDO().setName(wxMenu.getName())
                : MpMenuConvert.INSTANCE.convert02(wxMenu);
        // 设置菜单的公众号账号信息
        if (account != null) {
            menu.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        // 设置父编号
        if (parentMenu != null) {
            menu.setParentId(parentMenu.getId());
        } else {
            menu.setParentId(MpMenuDO.ID_ROOT);
        }

        // 插入到数据库
        mpMenuMapper.insert(menu);
        return menu;
    }

    /**
     * 校验菜单的格式是否正确
     *
     * @param menu 菜单
     */
    private void validateMenu(MpMenuSaveReqVO.Menu menu) {
        // 有子类的话，我就不对父类进行校验了 // TODO 项目经理-蓝欣 这里校验存在问题，我必须需要一个更加合理的校验方式，例如：我添加两个父，一个子
        if(CollUtil.isEmpty(menu.getChildren())){
            MpUtils.validateButton(validator, menu.getType(), menu.getReplyMessageType(), menu);
        } else {
            // 子菜单校验
            menu.getChildren().forEach(this::validateMenu);
        }

    }

}
