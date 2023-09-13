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
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftItem;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftList;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        // 返回分页
        return success(new PageResult<>(draftList.getItems(), draftList.getTotalCount().longValue()));
    }



}
