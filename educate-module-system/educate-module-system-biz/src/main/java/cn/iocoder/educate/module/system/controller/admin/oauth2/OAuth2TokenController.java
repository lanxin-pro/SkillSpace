package cn.iocoder.educate.module.system.controller.admin.oauth2;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.client.OAuth2ClientRespVO;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenRespVO;
import cn.iocoder.educate.module.system.convert.auth.OAuth2TokenConvert;
import cn.iocoder.educate.module.system.convert.oauth2.OAuth2ClientConvert;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.educate.module.system.service.auth.AdminAuthService;
import cn.iocoder.educate.module.system.service.oauth2.OAuth2TokenService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/7 17:48
 */
@Tag(name = "管理后台 - OAuth2.0 令牌")
@RestController
@RequestMapping("/system/oauth2-token")
public class OAuth2TokenController {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Resource
    private AdminAuthService adminAuthService;

    @Resource
    private AdminUserService adminUserService;

    @GetMapping("/page")
    @Operation(summary = "获得访问令牌分页", description = "只返回有效期内的")
    public CommonResult<PageResult<OAuth2AccessTokenRespVO>> getAccessTokenPage(@Valid OAuth2AccessTokenPageReqVO reqVO) {
        PageResult<OAuth2AccessTokenDO> pageResult = oauth2TokenService.getAccessTokenPage(reqVO);
        // 拼接结果返回
        ArrayList<OAuth2AccessTokenRespVO> oAuth2Clist = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(oAuth -> {
            OAuth2AccessTokenRespVO oAuth2ClientRespVO = OAuth2ClientConvert.INSTANCE.convert(oAuth);
            String nickname = adminUserService.getUserNickname(oAuth2ClientRespVO.getUserId());
            oAuth2ClientRespVO.setNickname(nickname);
            oAuth2Clist.add(oAuth2ClientRespVO);
        });
        return success(new PageResult<>(oAuth2Clist, pageResult.getTotal()));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "tudou")
    public CommonResult<Boolean> deleteAccessToken(@RequestParam("accessToken") String accessToken) {
        adminAuthService.logout(accessToken, LoginLogTypeEnum.LOGOUT_DELETE.getType());
        return CommonResult.success(true);
    }

}
