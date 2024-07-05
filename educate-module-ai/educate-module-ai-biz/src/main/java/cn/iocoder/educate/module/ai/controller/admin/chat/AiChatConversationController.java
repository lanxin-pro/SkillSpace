package cn.iocoder.educate.module.ai.controller.admin.chat;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author j-sentinel
 * @date 2024/7/2 15:41
 */
@RestController
@RequestMapping("/ai/chat/conversation")
@Validated
public class AiChatConversationController {

    // ========== 对话管理 ==========

    @GetMapping("/page")
    public String getChatConversationPage() {
        return "Hello World";
    }

}
