package cn.iocoder.educate.module.ai.service.chat;

import cn.iocoder.educate.module.ai.dal.dataobject.chat.AiChatConversationDO;
import cn.iocoder.educate.module.ai.dal.mysql.chat.AiChatConversationMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * AI 聊天对话 Service 实现类
 *
 * @author j-sentinel
 * @date 2024/7/7 15:28
 */
@Service
@Validated
@Slf4j
public class AiChatConversationServiceImpl implements AiChatConversationService {

    @Resource
    private AiChatConversationMapper chatConversationMapper;

    @Override
    public AiChatConversationDO getChatConversation(Long id) {
        return chatConversationMapper.selectById(id);
    }

    @Override
    public List<AiChatConversationDO> getChatConversationListByUserId(Long loginUserId) {
        return chatConversationMapper.selectListByUserId(loginUserId);
    }

}
