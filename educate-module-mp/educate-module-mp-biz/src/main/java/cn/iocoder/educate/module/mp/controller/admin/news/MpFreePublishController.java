package cn.iocoder.educate.module.mp.controller.admin.news;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.object.PageUtils;
import cn.iocoder.educate.module.mp.controller.admin.news.vo.MpFreePublishPageReqVO;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.service.material.MpMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishItem;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishList;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/14 14:42
 */
@Tag(name = "管理后台 - 公众号发布能力")
@RestController
@RequestMapping("/mp/free-publish")
@Validated
public class MpFreePublishController {

    @Resource
    private MpServiceFactory mpServiceFactory;

    @Resource
    private MpMaterialService mpMaterialService;

    @GetMapping("/page")
    @Operation(summary = "获得已发布的图文分页")
    @PreAuthorize("@lanxin.hasPermission('mp:free-publish:query')")
    public CommonResult<PageResult<WxMpFreePublishItem>> getFreePublishPage(MpFreePublishPageReqVO reqVO) {
        // 从公众号查询已发布的图文列表
        WxMpService mpService = mpServiceFactory.getRequiredMpService(reqVO.getAccountId());
        WxMpFreePublishList publicationRecords;
        try {
            // 第一个参数：分页页数，从0开始 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
            // 所以我要自己来编写分页算法 1-1 * 10    10-1 * 20
            publicationRecords = mpService.getFreePublishService().getPublicationRecords(
                    PageUtils.getStart(reqVO), reqVO.getPageSize());
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FREE_PUBLISH_LIST_FAIL,
                    e.getError().getErrorMsg());
        }

        // 返回分页
        return success(new PageResult<>(publicationRecords.getItems(), publicationRecords.getTotalCount().longValue()));
    }

    @PostMapping("/submit")
    @Operation(summary = "发布草稿")
    @Parameters({
            @Parameter(name = "accountId", description = "公众号账号的编号", required = true, example = "1024"),
            @Parameter(name = "mediaId", description = "要发布的草稿的 media_id", required = true, example = "2048")
    })
    @PreAuthorize("@lanxin.hasPermission('mp:free-publish:submit')")
    public CommonResult<String> submitFreePublish(@RequestParam("accountId") Long accountId,
                                                  @RequestParam("mediaId") String mediaId) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            String publishId = mpService.getFreePublishService().submit(mediaId);
            return success(publishId);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FREE_PUBLISH_SUBMIT_FAIL,
                    e.getError().getErrorMsg());
        }
    }


}
