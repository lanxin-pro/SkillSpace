package cn.iocoder.educate.module.system.controller.admin.permission;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.*;
import cn.iocoder.educate.module.system.convert.permission.MenuConvert;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.service.permission.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 17:05
 */
@Tag(name = "管理后台 - 菜单")
@RestController
@RequestMapping("/system/menu")
@Validated
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    public CommonResult<Long> createMenu(@Valid @RequestBody MenuCreateReqVO reqVO) {
        Long menuId = menuService.createMenu(reqVO);
        return success(menuId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改菜单")
    public CommonResult<Boolean> updateMenu(@Valid @RequestBody MenuUpdateReqVO reqVO) {
        menuService.updateMenu(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除菜单")
    @Parameter(name = "id", description = "角色编号", required= true, example = "1024")
    public CommonResult<Boolean> deleteMenu(@RequestParam("id") Long id) {
        menuService.deleteMenu(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取菜单列表", description = "用于【菜单管理】界面")
    public CommonResult<List<MenuRespVO>> getMenuList(MenuListReqVO reqVO) {
        List<MenuDO> list = menuService.getMenuList(reqVO);
        // 首要排序
        list.sort(Comparator.comparing(MenuDO::getSort));
        return success(MenuConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取菜单精简信息列表", description = "只包含被开启的菜单，用于【角色分配菜单】功能的选项。" +
            "在多租户的场景下，会只返回租户所在套餐有的菜单")
    public CommonResult<List<MenuSimpleRespVO>> getSimpleMenuList() {
        // 获得菜单列表，只要开启状态的
        MenuListReqVO menuListReqVO = new MenuListReqVO();
        menuListReqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<MenuDO> list = menuService.getMenuListByTenant(menuListReqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(MenuDO::getSort));
        return success(MenuConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/get")
    @Operation(summary = "获取菜单信息")
    public CommonResult<MenuRespVO> getMenu(Long id) {
        MenuDO menu = menuService.getMenu(id);
        return success(MenuConvert.INSTANCE.convert(menu));
    }

}
