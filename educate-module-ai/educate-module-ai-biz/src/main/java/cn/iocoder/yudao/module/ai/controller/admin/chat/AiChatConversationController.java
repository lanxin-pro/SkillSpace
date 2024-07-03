//package cn.iocoder.yudao.module.ai.controller.admin.chat;
//
//import cn.iocoder.educate.framework.common.pojo.CommonResult;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
///**
// * @author j-sentinel
// * @date 2024/7/2 15:41
// */
//@Tag(name = "管理后台 - AI 聊天对话")
//@RestController
//@RequestMapping("/ai/chat/conversation")
//@Validated
//public class AiChatConversationController {
//
//    // ========== 对话管理 ==========
//
//    @GetMapping("/page")
//    @Operation(summary = "获得对话分页", description = "用于【对话管理】菜单")
//    public CommonResult<String> getChatConversationPage() {
//        return CommonResult.success("Hello World");
//    }
//
//}
