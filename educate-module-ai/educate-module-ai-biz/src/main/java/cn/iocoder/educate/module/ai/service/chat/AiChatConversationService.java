package cn.iocoder.educate.module.ai.service.chat;

import cn.iocoder.educate.module.ai.dal.dataobject.chat.AiChatConversationDO;

import java.util.List;

/**
 * AI 聊天对话 Service 接口
 *
 * @author j-sentinel
 * @date 2024/7/7 15:28
 */
public interface AiChatConversationService {

    /**
     * 获得聊天对话
     *
     * @param id 编号
     * @return 聊天对话
     */
    AiChatConversationDO getChatConversation(Long id);

    /**
     * 获得【我的】聊天对话列表
     *
     * @param loginUserId 用户编号
     * @return 聊天对话列表
     */
    List<AiChatConversationDO> getChatConversationListByUserId(Long loginUserId);
}
