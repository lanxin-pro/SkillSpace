package cn.iocoder.educate.module.ai.controller.admin.chat;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.util.object.BeanUtils;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.ai.controller.admin.chat.vo.conversation.AiChatConversationRespVO;
import cn.iocoder.educate.module.ai.dal.dataobject.chat.AiChatConversationDO;
import cn.iocoder.educate.module.ai.service.chat.AiChatConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/7/2 15:41
 */
@Tag(name = "管理后台 - AI 聊天对话")
@RestController
@RequestMapping("/ai/chat/conversation")
@Validated
public class AiChatConversationController {

    @Resource
    private AiChatConversationService chatConversationService;

    @GetMapping("/get-my")
    @Operation(summary = "获得【我的】聊天对话")
    @Parameter(name = "id", required = true, description = "对话编号", example = "1024")
    public CommonResult<AiChatConversationRespVO> getChatConversationMy(@RequestParam("id") Long id) {
        AiChatConversationDO conversation = chatConversationService.getChatConversation(id);
        if (conversation != null && ObjUtil.notEqual(conversation.getUserId(),
                SecurityFrameworkUtils.getLoginUserId())) {
            conversation = null;
        }
        return CommonResult.success(BeanUtils.toBean(conversation, AiChatConversationRespVO.class));
    }

    @GetMapping("/my-list")
    @Operation(summary = "获得【我的】聊天对话列表")
    public CommonResult<List<AiChatConversationRespVO>> getChatConversationMyList() {
        List<AiChatConversationDO> list = chatConversationService
                .getChatConversationListByUserId(SecurityFrameworkUtils.getLoginUserId());
        return CommonResult.success(BeanUtils.toBean(list, AiChatConversationRespVO.class));
    }
}
