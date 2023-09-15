package cn.iocoder.educate.module.mp.controller.admin.news;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.framework.common.util.object.PageUtils;
import cn.iocoder.educate.module.mp.controller.admin.news.vo.MpDraftPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.service.material.MpMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.*;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/12 16:46
 */
@Tag(name = "管理后台 - 公众号草稿")
@RestController
@RequestMapping("/mp/draft")
@Validated
public class MpDraftController {

    @Resource
    private MpServiceFactory mpServiceFactory;

    @Resource
    private MpMaterialService mpMaterialService;

    @GetMapping("/page")
    @Operation(summary = "获得草稿分页")
    @PreAuthorize("@lanxin.hasPermission('mp:draft:query')")
    public CommonResult<PageResult<WxMpDraftItem>> getDraftPage(MpDraftPageReqVO reqVO) {
        // 从公众号查询草稿箱
        WxMpService mpService = mpServiceFactory.getRequiredMpService(reqVO.getAccountId());
        WxMpDraftList draftList;
        try {
            // 第一个参数：分页页数，从0开始 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
            // 所以我要自己来编写分页算法 1-1 * 10    10-1 * 20
            draftList = mpService.getDraftService().listDraft(PageUtils.getStart(reqVO), reqVO.getPageSize());
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DRAFT_LIST_FAIL, e.getError().getErrorMsg());
        }

        // 查询对应的图片地址。目的：解决公众号的图片链接无法在我们后台展示
        setDraftThumbUrl(draftList.getItems());

        // 返回分页
        return success(new PageResult<>(draftList.getItems(), draftList.getTotalCount().longValue()));
    }

    /**
     * {
     *     "articles": [
     *         {
     *             "title":TITLE,
     *             "author":AUTHOR,
     *             "digest":DIGEST,
     *             "content":CONTENT,
     *             "content_source_url":CONTENT_SOURCE_URL,
     *             "thumb_media_id":THUMB_MEDIA_ID,
     *             "need_open_comment":0,
     *             "only_fans_can_comment":0
     *         }
     *         //若新增的是多图文素材，则此处应还有几段articles结构
     *     ]
     * }
     * @param accountId
     * @param draft
     * @return
     */
    @PostMapping("/create")
    @Operation(summary = "创建草稿")
    @Parameter(name = "accountId", description = "公众号账号的编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('mp:draft:create')")
    public CommonResult<String> deleteDraft(@RequestParam("accountId") Long accountId,
                                            @RequestBody WxMpAddDraft draft) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            String mediaId = mpService.getDraftService().addDraft(draft);
            return success(mediaId);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DRAFT_CREATE_FAIL, e.getError().getErrorMsg());
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新草稿")
    @Parameters({
            @Parameter(name = "accountId", description = "公众号账号的编号", required = true, example = "1024"),
            @Parameter(name = "mediaId", description = "草稿素材的编号", required = true, example = "xxx")
    })
    @PreAuthorize("@lanxin.hasPermission('mp:draft:update')")
    public CommonResult<Boolean> deleteDraft(@RequestParam("accountId") Long accountId,
                                             @RequestParam("mediaId") String mediaId,
                                             @RequestBody List<WxMpDraftArticles> articles) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            for (int i = 0; i < articles.size(); i++) {
                WxMpDraftArticles article = articles.get(i);
                mpService.getDraftService().updateDraft(new WxMpUpdateDraft(mediaId, i, article));
            }
            return success(true);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DRAFT_UPDATE_FAIL, e.getError().getErrorMsg());
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除草稿")
    @Parameters({
            @Parameter(name = "accountId", description = "公众号账号的编号", required = true, example = "1024"),
            @Parameter(name = "mediaId", description = "草稿素材的编号", required = true, example = "xxx")
    })
    @PreAuthorize("@lanxin.hasPermission('mp:draft:delete')")
    public CommonResult<Boolean> deleteDraft(@RequestParam("accountId") Long accountId,
                                             @RequestParam("mediaId") String mediaId) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            mpService.getDraftService().delDraft(mediaId);
            return success(true);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DRAFT_DELETE_FAIL, e.getError().getErrorMsg());
        }
    }

    private void setDraftThumbUrl(List<WxMpDraftItem> items) {
        // 1.1 获得 mediaId 数组
        Set<String> mediaIds = new HashSet<>();
        items.forEach(item -> item.getContent().getNewsItem().forEach(newsItem -> {
            mediaIds.add(newsItem.getThumbMediaId());
        }));
        if (CollUtil.isEmpty(mediaIds)) {
            return;
        }
        // 1.2 批量查询对应的 Media 素材
        Map<String, MpMaterialDO> materials = mpMaterialService.getMaterialListByMediaId(mediaIds)
                .stream()
                .collect(Collectors.toMap(MpMaterialDO::getMediaId, Function.identity(),(v1,v2) -> v1));

        // 2. 设置回 WxMpFreePublishItem 记录
        items.forEach(item -> {
            item.getContent().getNewsItem().forEach(newsItem ->
                // 从哈希表查找到 key 对应的 value，然后进一步处理
                MapUtils.findAndThen(materials, newsItem.getThumbMediaId(),
                    material -> {
                        newsItem.setThumbUrl(material.getUrl());
                    }));
        });
    }

}
